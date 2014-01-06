package org.middleheaven.quantity.math.structure;

/**
 * Marks a class as belonging to a ring
 * and have inverse capabilities.
 * 
 *
 * @param <T> the tppe of the element
 */
public interface GroupMultiplicative<T>  {

	/**
	 * The devision of this by other.
	 * @param other the quociente.
	 * @return this / other
	 */
	public T over(T other);
	
	/**
	 * The multiplication inverse.
	 * @return 1/ this.
	 */
	public T inverse();
}
