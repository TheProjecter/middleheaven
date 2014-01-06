/**
 * 
 */
package org.middleheaven.collections.range;

import org.middleheaven.collections.ComparableComparator;
import org.middleheaven.util.Incrementor;

/**
 * 
 */
public abstract class RangeBuilder<T extends Comparable, I> {

	protected T start;
	protected Incrementor<T, I> incrementor;

	RangeBuilder(T start, Incrementor<T, I> incrementor){
		this.start = start;
		this.incrementor = incrementor;
	}
	
	public RangeBuilder<T, I> by(I step){
		
		if (incrementor == null){
			throw new IllegalStateException("Incrementor is not defined. Define an Incrementor prior to define it's step");
		}
		this.incrementor = this.incrementor.withStep(step);
		return this;
	}
	
	public RangeBuilder<T, I> withIncrementor(Incrementor<T,I> incrementor){
		this.incrementor = incrementor;
		return this;
	}
	
	public final Range<T, I> upTo(T end){
		if (incrementor == null){
			throw new IllegalStateException("Incrementor is not defined. Define an Incrementor prior to define the end element of the range");
		}
		if (!isStepDefined()){
			throw new IllegalArgumentException("Is necessary to define an increment step");
		}
		return new Range<T,I> ( start, end , ComparableComparator.<T>getInstance(), this.incrementor);
	}
	

	/**
	 * @return
	 */
	protected boolean isStepDefined() {
		return true;
	}
	

}
