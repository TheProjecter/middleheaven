package org.middleheaven.web.container;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.middleheaven.core.FileContext;
import org.middleheaven.core.bootstrap.AbstractBootstrapEnvironment;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.BootstrapEnvironment;
import org.middleheaven.core.bootstrap.EditableContainerFileRepositoryManager;
import org.middleheaven.io.ManagedIOException;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedFilePath;
import org.middleheaven.io.repository.ManagedFileRepository;

/**
 * Base abstraction for a {@link BootstrapEnvironment} that can process web requests based on a {@link ServletContext}.
 */
public abstract class WebContainerBootstrapEnvironment extends AbstractBootstrapEnvironment  {

	private ServletContext context;
	private EditableContainerFileRepositoryManager containerFileSystem;

	public WebContainerBootstrapEnvironment(ServletContext context) {
		this.context = context;
	}
	
	public void preConfigurate(BootstrapContext context) {
		//no-op
	}
	
	public ServletContext getServletContext(){
		return this.context;
	}

	@Override
	public final FileContext getFileContext() {
		if (this.containerFileSystem==null){
			containerFileSystem = new EditableContainerFileRepositoryManager();
			
			setupDefaultFilesRepositories(context,containerFileSystem);

			String fileMapping = this.context.getInitParameter("filesMapping");
			if(fileMapping!=null){
				ManagedFile file = containerFileSystem.getEnvironmentConfigRepository().retrive(fileMapping);
				if(file.exists()){
					setupConfigurationForFilesRepositories(file,containerFileSystem);
				}
			} 
			
			
		}
		
		return containerFileSystem;
	}
	
	protected void setupConfigurationForFilesRepositories(ManagedFile configFile, EditableContainerFileRepositoryManager fileSystem){

		Properties params = new Properties();
		try {
			params.load(configFile.getContent().getInputStream());

			fileSystem.setEnvironmentConfigRepository(resolveFile("environmentConfig", params , fileSystem.getEnvironmentConfigRepository() , fileSystem.getAppRootRepository()));
			fileSystem.setEnvironmentDataRepository(resolveFile("environmentData" , params , fileSystem.getEnvironmentDataRepository(), fileSystem.getAppRootRepository()));

			fileSystem.setAppConfigRepository(resolveFile("appConfig" , params , fileSystem.getAppConfigRepository(), fileSystem.getAppRootRepository()));
			fileSystem.setAppDataRepository(resolveFile("appData"  , params ,fileSystem.getAppDataRepository(), fileSystem.getAppRootRepository()));
			fileSystem.setAppLogRepository(resolveFile("appLog" , params , fileSystem.getAppLogRepository(), fileSystem.getAppRootRepository()));
			fileSystem.setAppClasspathRepository(resolveFile("appClasspath" , params ,fileSystem.getAppClasspathRepository(), fileSystem.getAppRootRepository()));


		} catch (IOException e) {
			throw ManagedIOException.manage(e);
		}
	}

	
	protected ManagedFile getContainerRoot(ServletContext servletContext){
		
		servletContext.log("Creating root from: " + servletContext.getRealPath(""));
		
		ManagedFileRepository repo = this.getManagedFileRepositoryProvider().newRepository(
				new File(servletContext.getRealPath("")).toURI(),
				//URI.create("file://" + servletContext.getRealPath("").replace(File.separatorChar, '/')),
				null
		);
		
		ManagedFilePath path = repo.getRoots().iterator().next();
		
		return repo.retrive(path);
	}
	
	/**
	 * 
	 * @param context
	 * @param fileSystem
	 */
	protected void setupDefaultFilesRepositories(ServletContext context, EditableContainerFileRepositoryManager fileSystem ){
		ManagedFile root =getContainerRoot(context);

		fileSystem.setAppRootRepository(root);
		
		fileSystem.setEnvironmentConfigRepository(root.retrive("../../conf"));
		
		fileSystem.setEnvironmentDataRepository(
				root.retrive("../../data").createFolder()
		);
		
		fileSystem.setAppConfigRepository(root.retrive(("META-INF")));
		fileSystem.setAppDataRepository( // TODO must contain appID
				root.retrive("../../data").createFolder()
		);
		
		// TODO must contain appID
		fileSystem.setAppLogRepository(fileSystem.getAppDataRepository().retrive("log").createFolder());
		
		fileSystem.setAppClasspathRepository(root.retrive("WEB-INF/classes"));
		
		fileSystem.setAppLibraryRepository(root.retrive("WEB-INF/lib"));
		
		fileSystem.setAppWebRestrictedRootRepository(root.retrive("WEB-INF"));
		
		fileSystem.setAppWebRootRepository(root);
	}
	
	
	private ManagedFile resolveFile(String name, Properties params, ManagedFile defaultLocation, ManagedFile root) {
		String path = (String)params.get(name);
		if(path == null || path.trim().isEmpty()){
			return defaultLocation;
		}

		// relative path
		if (path.startsWith(".")){
			return root.retrive(path).createFolder();
		} else {
			// absolute path
			final ManagedFileRepository repo = this.getManagedFileRepositoryProvider().newRepository(URI.create(path), null);
			
			return repo.retrive(repo.getPath(""));

		}
	}

	

	@Override
	public void start() {
		// no-op
	}

	@Override
	public void stop(){
		this.context = null; 
	}
	

	public abstract WebContainerInfo getWebContainerInfo();
		


}
