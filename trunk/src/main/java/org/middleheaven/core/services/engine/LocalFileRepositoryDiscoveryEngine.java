package org.middleheaven.core.services.engine;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.discover.ServiceActivator;
import org.middleheaven.core.services.discover.ServiceActivatorDiscoveryEngine;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFileFilter;
import org.middleheaven.io.repository.service.FileRepositoryService;

public class LocalFileRepositoryDiscoveryEngine extends ServiceActivatorDiscoveryEngine {

	
	protected List<Class<ServiceActivator>> discoverActivators(ServiceContext context){
		FileRepositoryService frs = context.getService(FileRepositoryService.class, null);

		
		ManagedFile f = frs.getRepository("ENV_CONFIGURATION");
		
		Collection<ManagedFile> serviceJars = new HashSet<ManagedFile>();
		serviceJars.addAll( f.listFiles(new ManagedFileFilter(){

			@Override
			public Boolean classify(ManagedFile file) {
				return file.getName().endsWith(".srv");
			}

		}));
		f = frs.getRepository("APP_CONFIGURATION");

		serviceJars.addAll( f.listFiles(new ManagedFileFilter(){

			@Override
			public Boolean classify(ManagedFile file) {
				return file.getName().endsWith(".srv");
			}

		}));

		List<Class<ServiceActivator>> activatorsClasses = new LinkedList<Class<ServiceActivator>>();
		
		URLClassLoader cloader ;
		for (ManagedFile jar : serviceJars){
			try {
				try{
				    cloader = URLClassLoader.newInstance(new URL[]{jar.getURL()});
					
					JarInputStream jis = new JarInputStream(jar.getContent().getInputStream());
					Manifest manifest = jis.getManifest();
					Attributes at = manifest.getMainAttributes();
					
					String className = at.getValue("MiddleHeaven-Activator");
					
					if(className!=null && !className.isEmpty()){
						activatorsClasses.add((Class<ServiceActivator>) cloader.loadClass(className));
					}

				}catch (IOException e) {
					ManagedIOException.manage(e);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ManagedIOException e) {
				// TODO log and continue
				e.printStackTrace();
			} 
		}
		
		return activatorsClasses;
	}
	


	

}
