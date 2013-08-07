/**
 * 
 */
package org.middleheaven.licence;

import java.security.PrivilegedAction;

/**
 * 
 */
public final class AddocClassLoaderPrivilegedAction implements PrivilegedAction<AddocClassLoader> {

	private static final AddocClassLoaderPrivilegedAction action = new AddocClassLoaderPrivilegedAction();
	
	public static AddocClassLoaderPrivilegedAction getInstance(){
		return action;
	}
	
	private AddocClassLoaderPrivilegedAction(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AddocClassLoader run() {
		return new AddocClassLoader();
	}

}
