package org.middleheaven.licence;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashSet;

import org.middleheaven.core.FileContext;
import org.middleheaven.core.FileContextService;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.crypto.Base64CipherAlgorithm;
import org.middleheaven.io.IO;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.logging.Logger;
import org.middleheaven.util.function.Block;


public class LicenseServiceActivator extends ServiceActivator {

	private LicenseService implementation;
	private LicenseProvider provider = new VoidLicenseProvider();

	public LicenseServiceActivator (){}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
		implementation = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(BootstrapService.class));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(LicenseService.class));
	}

	@Override
	public void activate(ServiceContext serviceContext) {

		FileContext frs = serviceContext.getService(FileContextService.class).getFileContext();

		ManagedFile f = frs.getEnvironmentConfigRepository();

		final Collection<ManagedFile> licences = new HashSet<ManagedFile>();

		
		for(ManagedFile file : f.children()){
			if (file.getPath().getFileNameExtension().equals(".lic")){
				licences.add(file);
			}
		}

		f = frs.getAppConfigRepository();
		
		for(ManagedFile file : f.children()){
			if (file.getPath().getFileNameExtension().equals(".lic")){
				licences.add(file);
			}
		}
		
		// search certificates
		final Collection<ManagedFile> certifcates = new HashSet<ManagedFile>();

		for(ManagedFile file : f.children()){
			if (file.getPath().getFileNameExtension().equals(".cert")){
				certifcates.add(file);
			}
		}
		

		if (licences.isEmpty() || certifcates.isEmpty()){
			provider = new VoidLicenseProvider();
		} else {
			provider = load( new ComposedProvider() , licences ,certifcates.iterator().next() );
		}

		implementation = new LicenceServiceImpl(provider);

		//context.addServiceListener(new ServiceLock());

		serviceContext.register(LicenseService.class, implementation);
	}

//	private static class ServiceLock implements ServiceListener{
//
//		private int alreadySet = 0; 
//		@Override
//		public void onEvent(ServiceEvent event) {
//			if (event.getServiceClass().getName().equals(LicenseService.class.getName())){
//				if (event.getType().equals(ServiceEventType.ADDED)){
//					if (alreadySet>0){
//						throw new SecurityException("LicenceService is locked");
//					}
//
//					alreadySet++;
//				} else {
//					alreadySet--;
//				}
//			}
//
//		}
//
//	}



	private ComposedProvider load(final ComposedProvider providers ,Collection<ManagedFile> licences , ManagedFile certificate ){


		for (ManagedFile f : licences){
			// open file and decipher it with key

			try {
				IO.using(new BufferedReader(new InputStreamReader(f.getContent().getInputStream())), new BufferedReaderBlock(providers));
			} catch (Exception e) {
				Logger.onBookFor(this.getClass()).error(e,"Licence {0} could not be loaded", f.getURI());
			} 


			// read provider class

			// read properties data

			// add reader provider to providers

		}
		return providers;
	}

	private static class BufferedReaderBlock implements Block<BufferedReader> {

		private ComposedProvider providers;

		
		public BufferedReaderBlock(ComposedProvider providers) {
			super();
			this.providers = providers;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void apply(BufferedReader reader) {
			try {
				final AddocClassLoader cloader = AccessController.doPrivileged(AddocClassLoaderPrivilegedAction.getInstance());
				
				Base64CipherAlgorithm base64 = new Base64CipherAlgorithm();

				StringBuilder buffer = new StringBuilder();
				String line;
				while ((line = reader.readLine())!=null){
					buffer.append(line);
				}

				String classDef = buffer.substring(buffer.indexOf("<class>")+ "<class>".length(), buffer.indexOf("</class>"));

				ByteArrayInputStream in = new ByteArrayInputStream(base64.revertCipher(classDef.getBytes()));

				ByteArrayOutputStream classDefStream = new ByteArrayOutputStream();

				ClassDefinition cd  = new ClassDefinition();
				String className = cd.read(in, classDefStream);

				cloader.addClassData(className, classDefStream.toByteArray());

				Object obj = cloader.loadClass(className).newInstance();

				SerializableLicenseProvider p = Introspector.of(SerializableLicenseProvider.class)
						.newProxyInstance(LicenceProviderHandler.wrapp(obj));

				String data = buffer.substring(buffer.indexOf("<data>")+ "<data>".length(), buffer.indexOf("</data>"));

				p.setAttributes(data);

				providers.addProvider(p);
			} catch (IOException e){
				throw ManagedIOException.manage(e);
			} catch (Exception e){
				throw new RuntimeException(e);
			}
		}
		
	}
	
	private static final class LicenceProviderHandler implements ProxyHandler{

		Object provider;
		Method getLicenceMethod;
		Method setAttributesMethod;

		public static LicenceProviderHandler wrapp(Object provider) throws SecurityException, NoSuchMethodException{
			LicenceProviderHandler me =  new LicenceProviderHandler();

			me.provider =provider;
			me.getLicenceMethod = provider.getClass().getMethod("getLicence", String.class,String.class);
			me.setAttributesMethod = provider.getClass().getMethod("setAttributes", String.class);

			return me;
		}

		private LicenceProviderHandler(){
		}

		@Override
		public Object invoke(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable {
			if (delegator.getName().equals(getLicenceMethod.getName())){
				try{
					Object objx=  getLicenceMethod.invoke(provider, args);

					return objx;
				} catch (Exception t){
					return null;
				}
			} else if (delegator.getName().equals(setAttributesMethod.getName())){
				return setAttributesMethod.invoke(provider, args);
			} else {
				return null;
			}
		}

	}


	private static class LicenceServiceImpl implements LicenseService{

		private LicenseProvider provider;
		
		public LicenceServiceImpl( LicenseProvider provider){
			this.provider = provider;
		}

		@Override
		public License getLicence(String featureID, String version) {
			return 	provider.getLicence(featureID, version);
		}

	}







}
