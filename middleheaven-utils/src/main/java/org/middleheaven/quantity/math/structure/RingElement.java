package org.middleheaven.quantity.math.structure;

/**
 * Marks a class as belonging to a ring.
 *
 * @param <T> the type of the element in the group.
 */
public interface RingElement<T extends RingElement<T>> extends GroupAdditiveElement<T> {

	/**
	 * Multiplication
	 * @param other factor
	 * @return <code>this</code> times <code>other</code>
	 */
	public T times(T other);

	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public Ring<T> getAlgebricStructure();
}
