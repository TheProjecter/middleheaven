/**
 * 
 */
package org.middleheaven.application;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.collections.ParamsMap;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.io.IO;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.JarFile;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.util.Splitter;
import org.middleheaven.util.Version;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class LibraryJarModuleScanner implements ModuleScanner {

	private ManagedFile libraryFolder;
	
	/**
	 * 
	 * Constructor.
	 * @param libraryFolder the folder point to the application folder.
	 */
	public LibraryJarModuleScanner (ManagedFile libraryFolder){
		this.libraryFolder = libraryFolder;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scan(Collection<Module> modules) {
	
		
		final Collection<JarFile> applicationModuleFiles = new HashSet<JarFile>();

//		if (libraryFolder.isWatchable()){
//			WatchService ws = libraryFolder.getRepository().getWatchService();
//
//			WatchEventChannel channel = libraryFolder.register(ws , StandardWatchEvent.ENTRY_CREATED, StandardWatchEvent.ENTRY_DELETED, StandardWatchEvent.ENTRY_MODIFIED);
//
//			this.fileWatchChannelProcessor = new FileWatchChannelProcessor(new FileChangeStrategy() {
//
//				@Override
//				public void onChange(WatchEvent event) {
//					if (StandardWatchEvent.ENTRY_CREATED.equals(event.kind())){
//						ManagedFile file = event.getFile();
//						if (appModulesFilter.classify(file)){
//							setState(ApplicationCycleState.PAUSED);
//							loadModuleFromFile(file);
//							setState(ApplicationCycleState.READY);
//						}
//					}
//				}
//			});
//
//			fileWatchChannelProcessor.add(channel);	
//
//			fileWatchChannelProcessor.start();
//		}

		for (ManagedFile file : libraryFolder.children()){
			if (appModulesFilter.apply(file)){
				applicationModuleFiles.add(JarFile.from(file));
			}
		}

		for (JarFile jar : applicationModuleFiles){
			loadModuleFromFile(jar , modules);
		}

		
	}

	private void loadModuleFromFile(final JarFile jar, final Collection<Module> modules) {

		try{
			final URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURI().toURL()}, Thread.currentThread().getContextClassLoader());
			
			Manifest manifest = jar.getManifest();

			if (manifest!=null){
				Attributes at = manifest.getMainAttributes();

				if (at.getValue("Application") != null){
					ParamsMap map = new ParamsMap()
					.setParam("Application", at.getValue("Application"))
					.setParam("Application-Modules", at.getValue("Application-Modules"))
					.setParam("Application-Module-Depends", at.getValue("Application-Module-Depends"))
					;
					
					parseAttributes(map, cloader, modules);
				}
			}
			
		}catch (IOException e) {
			ManagedIOException.manage(e);
		} 

	}
	
	private void parseAttributes(Map<String, String> attributes , ClassLoader cloader, Collection<Module> modules){

		String applicationId = attributes.get("Application");
		
		String applicationModulesSignature = attributes.get("Application-Modules");
		String applicationModulesDepends = attributes.get("Application-Module-Depends");
		
	
		parseAndAddModules(applicationId, applicationModulesSignature , applicationModulesDepends, cloader , modules);

	}
	
	
	
	private void parseAndAddModules(String applicationId, String applicationModulesSignature, String applicationModulesDepends, ClassLoader cloader, Collection<Module> modules) {

		
		if (applicationModulesSignature.trim().length() == 0){
			throw new IllegalStateException("No modules found");
		}
		
		Enumerable<String> types = Splitter.on(',').trim().split(applicationModulesSignature.trim());
		final Splitter splitter = Splitter.on(":").trim();
		
		for (String type : types){
			
			String[] parts = splitter.split(type.trim()).asArray(new String[3]);
			
			ModuleActivator m =  Introspector.of(ModuleActivator.class).load(parts[2],cloader).newInstance();
			
			Module module = new Module(applicationId, parts[0], Version.valueOf(parts[1]), m );
			
			modules.add(module);
			
			if (applicationModulesDepends != null){
		
				for (String depend : Splitter.on(',').split(applicationModulesDepends.trim())){
					
					String[] defs = splitter.split(depend.trim()).asArray(new String[3]);
					// TODO control corret number of params 
					ModuleVersion mv = new ModuleVersion(defs[0], Version.valueOf(defs[1]));
					
					boolean required = true;
					if (defs.length == 3) {
						required = defs[2].equalsIgnoreCase("required");
					} 
					module.addDependency(new ModuleDependency(mv, required));
				}
			}
			
		}

		
	}
	
	private static Predicate<ManagedFile> appModulesFilter =  new Predicate<ManagedFile>(){

		@Override
		public Boolean apply(ManagedFile file) {
			if (file.getPath().getFileName().endsWith(".jar")){
				try {
					return IO.using(new JarInputStream(file.getContent().getInputStream()), new Function<Boolean , JarInputStream>(){

						@Override
						public Boolean apply(JarInputStream jis) {

							Manifest manifest = jis.getManifest();

							return manifest != null ;
						}
						
					});
				} catch (ManagedIOException e) {
					return false;
				} catch (IOException e) {
					return false;
				}
			
			}
			return false;

		}
	};

}
