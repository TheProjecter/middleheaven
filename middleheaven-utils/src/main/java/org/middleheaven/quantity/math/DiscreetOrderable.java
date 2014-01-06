package org.middleheaven.quantity.math;

/**
 * Mark a value class as orderable and discreet.
 * For any given DiscreetOrderable is possible to determine the next and/or the previous value.
 * 
 * @see org.middleheaven.quantity.math.BigInt
 *
 * @param <N> the real type.  
 */
public interface DiscreetOrderable<N> {
	
	/**
	 * 
	 * @return the previous value on the order sequence
	 */
	public N previous();
	
	/**
	 * 
	 * @return the next value on the order sequence
	 */
	public N next();
}
