/**
 * 
 */
package org.middleheaven.io;

/**
 * 
 */
public class ModificationNotTracableIOException extends ManagedIOException {

	private static final long serialVersionUID = 3768579621629729440L;

	/**
	 * Constructor.
	 * @param msg
	 */
	public ModificationNotTracableIOException() {
		super("Modification is not tracable");
	}

}
