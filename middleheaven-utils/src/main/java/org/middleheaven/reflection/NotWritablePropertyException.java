/**
 * 
 */
package org.middleheaven.reflection;

/**
 * 
 */
public class NotWritablePropertyException extends RuntimeException {

	private static final long serialVersionUID = 639760013466050512L;

	/**
	 * Constructor.
	 * @param name
	 */
	public NotWritablePropertyException(String name) {
		super("Property " + name + " is read only");
	}


}
