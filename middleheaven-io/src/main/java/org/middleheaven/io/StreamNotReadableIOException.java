/**
 * 
 */
package org.middleheaven.io;

/**
 * 
 */
public class StreamNotReadableIOException extends ManagedIOException {

	private static final long serialVersionUID = -1917227023801316524L;

	/**
	 * Constructor.
	 * @param msg
	 */
	public StreamNotReadableIOException() {
		super("Stream is not readable");
	}

}
