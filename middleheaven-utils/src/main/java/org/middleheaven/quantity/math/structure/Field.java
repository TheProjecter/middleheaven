/**
 * 
 */
package org.middleheaven.quantity.math.structure;

import java.util.Random;

/**
 * 
 */
public interface Field<F extends FieldElement<F>> extends Ring<F> {

	/**
	 * 
	 * @param n
	 * @return
	 */
	public F fromNumber(Number n);

	/**
	 * Return fromNumber(Math.random);
	 * @return
	 */
	public F random();
	
	/**
	 * Return fromNumber(Random.next);
	 * @return
	 */
	public F random(Random random);
}
