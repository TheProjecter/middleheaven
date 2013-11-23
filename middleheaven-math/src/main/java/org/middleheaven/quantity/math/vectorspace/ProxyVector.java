/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.collections.enumerable.FastCountEnumerable;
import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
abstract class ProxyVector<F extends FieldElement<F>> extends AbstractVector<F> implements FastCountEnumerable{

	private final int size;

	/**
	 * Constructor.
	 * @param vectorSpace
	 */
	public ProxyVector(VectorSpace vectorSpace, int size) {
		super(vectorSpace);
		this.size = size;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}

}
