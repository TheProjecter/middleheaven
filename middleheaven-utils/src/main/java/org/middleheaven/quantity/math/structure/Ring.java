/**
 * 
 */
package org.middleheaven.quantity.math.structure;

/**
 * 
 */
public interface Ring<T extends RingElement<T>> extends GroupAdditive<T>{

	/**
	 * 
	 * @return the multiplication identity.
	 */
	public T one();
}
