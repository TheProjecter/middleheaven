/**
 * 
 */
package org.middleheaven.io;

/**
 * 
 */
public class StreamNotWritableIOException extends ManagedIOException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3667651102945746309L;

	/**
	 * Constructor.
	 */
	public StreamNotWritableIOException() {
		super("Stream is not writable");
	}

}
