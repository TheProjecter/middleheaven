/**
 * 
 */
package org.middleheaven.quantity.math.structure;

/**
 * 
 */
public interface GroupAdditive<A extends GroupAdditiveElement<A>> extends AlgebricStructure<A> {

	/**
	 * 
	 * @return the aditive identity element.
	 */
	public A zero();
}
