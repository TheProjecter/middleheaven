/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.collections.enumerable.FastCountEnumerable;
import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
abstract class LazyProxyVector<F extends FieldElement<F>> extends AbstractVector<F> implements FastCountEnumerable{

	private final int size;
	private final Object[] cache;
	
	/**
	 * Constructor.
	 * @param provider
	 */
	public LazyProxyVector(VectorSpace vectorSpace, int size) {
		super(vectorSpace);
		this.size = size;
		this.cache = new Object[size];
		
	}


	@Override
	public final F get(int index) {
		if (index >= this.size() || index < 0){
			throw new IndexOutOfBoundsException("Index " + index + ", Size: " + this.size());
		}
		
		Object value = cache[index]; 
		if (value == null){

			value = lazyGet(index);
			cache[index] = value;
		}
		
		return (F) value;
	}
	

	protected abstract F lazyGet(int index);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}

}
