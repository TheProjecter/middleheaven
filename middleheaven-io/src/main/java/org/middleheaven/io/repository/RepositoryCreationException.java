package org.middleheaven.io.repository;

import org.middleheaven.io.ManagedIOException;

public class RepositoryCreationException extends ManagedIOException {

	private static final long serialVersionUID = -2853554516007117619L;

	public RepositoryCreationException(Throwable cause) {
		super( "Impossible to create requested file repository",cause);
	}
	
	public RepositoryCreationException() {
		this( "Impossible to create requested file repository");
	}

	/**
	 * Constructor.
	 * @param string
	 */
	public RepositoryCreationException(String message) {
		super(message);
	}
}
