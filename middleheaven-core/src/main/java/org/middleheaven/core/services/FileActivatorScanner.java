package org.middleheaven.core.services;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import org.middleheaven.core.annotations.Wire;
import org.middleheaven.core.bootstrap.activation.AbstractActivatorScanner;
import org.middleheaven.core.bootstrap.activation.ActivatorScanner;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.IO;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.watch.FileChangeStrategy;
import org.middleheaven.io.repository.watch.FileWatchChannelProcessor;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.logging.Logger;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.function.Block;

/**
 * An {@link ActivatorScanner} the scans jar files for the <i>unit-activator</i> property in the manifest and loads the activator found.
 * 
 */
public class FileActivatorScanner extends AbstractActivatorScanner {

	private final ManagedFile root;
	private final Set<Pattern> filePatterns = new HashSet<Pattern>();
	private final FileWatchChannelProcessor fileWatchChannelProcessor;
	private final Collection<ManagedFile> allFiles = new HashSet<ManagedFile>();
	private final Map<String, ServiceActivator> activators = new HashMap<String,ServiceActivator>();

	
	@Wire private Logger logger;
	
	private WiringService wiringService;

	/**
	 * Constructs a {@link FileActivatorScanner} that scans all files present in the <code>root<code> folder that match the given <code>filePattern</code>.
	 *
	 * @param root the reference root folder where to find files to scan.
	 * @param filePatterns the file patterns to scan.
	 */
	public FileActivatorScanner(ManagedFile root, String ... filePatterns){

		this.root = root;
		for (String pattern : filePatterns){
			this.filePatterns.add(Pattern.compile(pattern));
		}

		fileWatchChannelProcessor = new FileWatchChannelProcessor(new FileChangeStrategy(){

			@Override
			public void onChange(WatchEvent event) {
				if (wiringService != null){
					if (StandardWatchEvent.ENTRY_CREATED.equals(event.kind())){
						loadModuleFromFile(wiringService,event.getFile(), activators);
					}
				}

				// TODO delete and modified
			}} 
		);



		fileWatchChannelProcessor.start();
	}

	@Override
	public void scan() {

		if(root.getType().isFolder()){

			root.children().forEach(new Block<ManagedFile>(){

				@Override
				public void apply(ManagedFile file) {
					if (file.isReadable() && file.getType().isFile()){
						for (Pattern pattern : filePatterns){
							if ( pattern.matcher(file.getPath().getFileNameWithoutExtension()).find()){
								allFiles.add(file);
							}
							return;
						}
					} 
				}

			});

		} else if(root.exists()){
			allFiles.add(root);
		}

		if (root.isWatchable()){
			
			WatchEventChannel channel = root.register(
					root.getRepository().getWatchService(),
					StandardWatchEvent.ENTRY_CREATED, 
					StandardWatchEvent.ENTRY_DELETED, 
					StandardWatchEvent.ENTRY_MODIFIED
			);
			fileWatchChannelProcessor.add(channel);		
		}

		for (ManagedFile file : allFiles){
			loadModuleFromFile(wiringService,file , activators);
		
		}

	}

	private void loadModuleFromFile(final WiringService wiringService, final ManagedFile jar , final Map<String,ServiceActivator> activators) {

		try{
			final URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURI().toURL()});

			IO.using(new JarInputStream(jar.getContent().getInputStream()), new Block<JarInputStream>(){

				@Override
				public void apply(JarInputStream jis) {

					Manifest manifest = jis.getManifest();
					String className=null;
					if (manifest!=null){
						Attributes at = manifest.getMainAttributes();
						className = at.getValue("unit-activator");
					}

					if(className!=null && !className.isEmpty()){
						try{
							// TODO must instantiate ? 
							Class<? extends ServiceActivator> type = Introspector.of(ServiceActivator.class).load(className,cloader).getIntrospected().getReflectedType();
							ServiceActivator activator = wiringService.getInstance(type);

							String activatorName = activator.getClass().getName();

							ServiceActivator older = activators.get(activatorName);
							if (older != null){
								// unload the old activator.
								fireDeployableLost(older.getClass());
								activators.remove(activator.getClass().getName());
							}
							activators.put(activator.getClass().getName(),activator);

							fireDeployableFound(activator.getClass());
						} catch (ClassCastException e){
							logger.warn("{0} is not a valid application module activator",className);
						}
					}else {
						logger.warn( "{0} does not present an application module.",jar.getPath().getFileNameWithoutExtension());
					}
				}
				
			});
			


		}catch (IOException e) {
			ManagedIOException.manage(e);
		}
	}

}
