package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;


public class RepositoryNotQueriableException extends ManagedIOException {

	 public RepositoryNotQueriableException(String repositoyClassName) {
	        super(repositoyClassName + " repository is not queriable");
	    }

}
