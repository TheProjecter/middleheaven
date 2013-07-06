/**
 * 
 */
package org.middleheaven.collections;

import org.middleheaven.util.Incrementor;
import org.middleheaven.util.NaturalIncrementable;

/**
 * 
 */
public class NaturalIncrementableIncrementor<T extends NaturalIncrementable<T>> implements Incrementor<T, Integer> {

	
	private boolean isReversed;

	public NaturalIncrementableIncrementor (){
		this(false);
	}
	
	private NaturalIncrementableIncrementor(boolean isReversed){
		this.isReversed = isReversed;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T increment(T object) {
		return isReversed ? object.previous() : object.next();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<T, Integer> reverse() {
		return new NaturalIncrementableIncrementor<T>(!this.isReversed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<T, Integer> withStep(Integer step) {
		throw new UnsupportedOperationException("withStep is not supported");
	}

	

}
