/**
 * 
 */
package org.middleheaven.domain.store;

/**
 * 
 */
public interface StoreActionCommiter {

	/**
	 * @param action
	 */
	void commit(StoreAction action);

}
