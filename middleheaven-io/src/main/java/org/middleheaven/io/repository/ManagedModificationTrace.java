/**
 * 
 */
package org.middleheaven.io.repository;

import org.middleheaven.quantity.time.TimePoint;


/**
 * 
 */
public interface ManagedModificationTrace {

	/**
	 * The TimePoint ( from the reference epoc) this file was last modified.
	 * 
	 * @return The TimePoint ( from the reference epoc) this file was last modified.
	 * 
	 */
	public TimePoint getLastModified();
	
	/**
	 * changes the lastModified TimePoint on this file.
	 * 
	 * @param timePoint the new TimePoint
	 */
	public void setLastModified(TimePoint timePoint);
}
