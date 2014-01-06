/**
 * 
 */
package org.middleheaven.runtime;

/**
 * 
 */
public interface RuntimeAdapter {

	public void add(ShutdownHook hook);
	public void remove(ShutdownHook hook);
	
}
