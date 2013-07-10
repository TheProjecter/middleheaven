/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.quantity.math.UnivariateFunction;
import org.middleheaven.quantity.math.structure.FieldElement;
import org.middleheaven.quantity.math.structure.GroupAdditiveElement;

/**
 * 
 */
public interface Vector<F extends FieldElement<F>> extends  Enumerable<F> , GroupAdditiveElement<Vector<F>>{
	
	public F get(int index);
	
	
	public VectorSpace<Vector<F> ,F> getVectorSpace();
	
	/**
	 * Multiplication by a scalar
	 * @param a scalar value
	 * @return this multiplied by <code>a</code>. A new vector is returned where each coordinate is multiplied by <code>a</code>. 
	 * 
	 */
	public Vector<F> times (F a);
	
	/**
	 * Shortcut for <code>this.getVectorSpace().dotProduct(other)</code>;
	 * 
	 * @param other
	 * @return
	 */
	public F times(Vector<F> other);

	
	/**
	 * The number of dimensions on the vector.
	 * @return
	 */
	public int size();

	/**
	 * Transform to an array.
	 * @param elements the elements of the vector.
	 * @return
	 */
	public F[] toArray(F[] elements);
	

	/**
	 * Multiplies this vector by other vector point by point 
	 * 
	 * @param other
	 * @return  result[i] = this[i] * other[i]
	 * 
	 * 
	 */
	public Vector<F> multiply(Vector<F> other);
	
	
	/**
	 * Applys a function the each element of the vector and produces a new vector with the result.
	 * @param function
	 * @return
	 */
	public <N extends FieldElement<N>> Vector<N> apply(UnivariateFunction <F, N> function);
}
