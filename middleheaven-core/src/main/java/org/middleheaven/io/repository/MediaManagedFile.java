package org.middleheaven.io.repository;


public interface MediaManagedFile extends ManagedFile {

    /**
     * 
     * @return a handler for the file content
     */
    public MediaStreamableContent getContent();


	
}
