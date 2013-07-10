/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
class LazyDelegationVector<F extends FieldElement<F>> extends AbstractVector<F> {

	
	private Object[] cache;
	private Vector<F> original;
	private ValueResolver<F> resolver;
	
	protected LazyDelegationVector(Vector<F> original, ValueResolver<F> resolver){
		super(original.getVectorSpace());
		this.original = original;
		this.resolver = resolver;
		this.cache = new Object[original.size()];
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public F get(int index) {
		
		if (index >= this.size()){
			throw new IndexOutOfBoundsException("Index " + index + ", Size: " + this.size());
		}
		
		Object value = cache[index]; 
		if (value == null){

			value = resolver.resolve(index, original);
			cache[index] = value;
		}
		
		return (F) value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.original.size();
	}




}
