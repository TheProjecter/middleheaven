/**
 * 
 */
package org.middleheaven.collections.range;

import org.middleheaven.quantity.math.structure.GroupAdditiveElement;
import org.middleheaven.util.Incrementor;

/**
 * 
 */
public class GroupAdditiveIncrementor<V extends GroupAdditiveElement<V>> implements Incrementor<V, V> {

	
	private V step;
	private boolean reversed;

	GroupAdditiveIncrementor(V zero){
		this(zero, false);
	}
	
	GroupAdditiveIncrementor(V step, boolean reversed ){
		this.step = step;
		this.reversed = reversed;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public V increment(V object) {
		return object.plus(step);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<V, V> reverse() {
		return new GroupAdditiveIncrementor<V>(step.negate(), !this.reversed);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<V, V> withStep(V step) {
		GroupAdditiveIncrementor<V> inc = new GroupAdditiveIncrementor<V>(step, false);
		if (this.reversed){
			return inc.reverse();
		}
		return inc;
	}

	/**
	 * @return
	 */
	public V getStep() {
		return this.step;
	}

}
