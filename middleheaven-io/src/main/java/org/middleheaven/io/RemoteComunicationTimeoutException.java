package org.middleheaven.io;

/**
 * Exception throwned when a remote comunication times out.
 */
public class RemoteComunicationTimeoutException extends ManagedIOException {


	private static final long serialVersionUID = 8494475706287896593L;

	protected RemoteComunicationTimeoutException(String msg) {
		super(msg);
	}




}
