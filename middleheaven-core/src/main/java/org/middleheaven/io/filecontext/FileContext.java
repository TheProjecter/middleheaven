package org.middleheaven.io.filecontext;

import org.middleheaven.io.repository.ManagedFile;


/**
 * Represent special meaningfull locations for the container.
 */
public interface FileContext {

    /**
     * 
     * @return ManagedFile representing a folder where to read environment configuration 
     */
    public ManagedFile getEnvironmentConfigRepository();
    
    /**
     * 
     * @return ManagedFile representing a folder where to read environment data 
     */
    public ManagedFile getEnvironmentDataRepository();
    
    /**
     * The top most application folder. If the application is packaged as an war file, this is the same as {@link #getAppWebRootRepository()}.
     * 
     * @return ManagedFile representing the top most folder of the aplication's reference 
     */
    public ManagedFile getAppRootRepository();

    /**
     * The web application folder. If the application is packaged as an war file, this is the same as {@link #getAppRootRepository()}.
     * 
     * @return ManagedFile representing the web application folder root.
     */
    public ManagedFile getAppWebRootRepository();
    
    /**
     * The web application folder that is not acessible by the web client.  If the application is packaged as an war file, this should point to the WEB-INF folder.
     * 
     * @return ManagedFile representing the web application folder root.
     */
    public ManagedFile getAppWebRestrictedRootRepository();
    
    /**
     * 
     * @return ManagedFile representing a folder where to read application configuration
     */
    public ManagedFile getAppConfigRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read/write application data
     */
    public ManagedFile getAppDataRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read/write application log
     */
    public ManagedFile getAppLogRepository();

    /**
     * 
     * @return ManagedFile representing a folder where to read application classapth
     */
    public ManagedFile getAppClasspathRepository();

    /**
     * 
     * @return ManagedFile representing the aplication's library folder 
     */
    public ManagedFile getAppLibraryRepository();
    
}
