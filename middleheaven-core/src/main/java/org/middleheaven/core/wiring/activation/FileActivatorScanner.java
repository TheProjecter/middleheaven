package org.middleheaven.core.wiring.activation;

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

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.watch.FileChangeStrategy;
import org.middleheaven.io.repository.watch.FileWatchChannelProcessor;
import org.middleheaven.io.repository.watch.StandardWatchEvent;
import org.middleheaven.io.repository.watch.WatchEvent;
import org.middleheaven.io.repository.watch.WatchEventChannel;
import org.middleheaven.io.repository.watch.Watchable;
import org.middleheaven.logging.Log;
import org.middleheaven.util.classification.BooleanClassifier;
import org.middleheaven.util.collections.Walker;

/**
 * An {@link ActivatorScanner} the scans jar files for the <i>unit-activator</i> property in the manifest and loads the activator found.
 * 
 */
public class FileActivatorScanner extends AbstractActivatorScanner {

	private final ManagedFile root;
	private final Set<Pattern> filePatterns = new HashSet<Pattern>();
	private final FileWatchChannelProcessor fileWatchChannelProcessor;
	private final Collection<ManagedFile> allFiles = new HashSet<ManagedFile>();
	private final Map<String, Activator> activators = new HashMap<String,Activator>();

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
	public void scan(WiringService wiringService) {

		this.wiringService = wiringService;

		if(root.getType().isFolder()){

			root.each(new Walker<ManagedFile>(){

				@Override
				public void doWith(ManagedFile file) {
					if (file.isReadable() && file.getType().isFile()){
						for (Pattern pattern : filePatterns){
							if ( pattern.matcher(file.getPath().getBaseName()).find()){
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

		Watchable wr = null;
		if (root.isWatchable()){
			wr = (Watchable)root;
		}

		for (ManagedFile file : allFiles){
			loadModuleFromFile(wiringService,file , activators);
			if (wr != null) {
				WatchEventChannel channel = wr.watch(StandardWatchEvent.ENTRY_CREATED, StandardWatchEvent.ENTRY_DELETED, StandardWatchEvent.ENTRY_MODIFIED);
				fileWatchChannelProcessor.add(channel);		
			}
		}

	}

	private void loadModuleFromFile(WiringService wiringService,ManagedFile jar , Map<String,Activator> activators) {

		try{
			URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{jar.getURI().toURL()});

			JarInputStream jis = new JarInputStream(jar.getContent().getInputStream());
			Manifest manifest = jis.getManifest();
			String className=null;
			if (manifest!=null){
				Attributes at = manifest.getMainAttributes();
				className = at.getValue("unit-activator");
			}

			if(className!=null && !className.isEmpty()){
				try{
					// TODO must instantiate ? 
					Class<? extends Activator> type = Introspector.of(Activator.class).load(className,cloader).getIntrospected();
					Activator activator = wiringService.getObjectPool().getInstance(type);

					String activatorName = activator.getClass().getName();

					Activator older = activators.get(activatorName);
					if (older != null){
						// unload the old activator.
						fireDeployableLost(older.getClass());
						activators.remove(activator.getClass().getName());
					}
					activators.put(activator.getClass().getName(),activator);

					fireDeployableFound(older.getClass());
				} catch (ClassCastException e){
					Log.onBookFor(this.getClass()).warn("{0} is not a valid application module activator",className);
				}
			}else {
				Log.onBookFor(this.getClass()).warn( "{0} does not present an application module.",jar.getPath().getBaseName());
			}

		}catch (IOException e) {
			ManagedIOException.manage(e);
		}
	}

}
