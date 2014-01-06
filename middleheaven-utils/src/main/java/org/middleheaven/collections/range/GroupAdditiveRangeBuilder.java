/**
 * 
 */
package org.middleheaven.collections.range;

import org.middleheaven.quantity.math.structure.GroupAdditiveElement;
import org.middleheaven.quantity.math.structure.RingElement;

/**
 * A {@link RangeBuilder} the can create ranges for any {@link GroupAdditiveElement} that is also {@link Comparable}.
 */
public class GroupAdditiveRangeBuilder<V extends GroupAdditiveElement<V> & Comparable> extends RangeBuilder<V, V> {

	/**
	 * Constructor.
	 * @param start
	 * @param incrementor
	 */
	GroupAdditiveRangeBuilder(V start) {
		super(start, getIncrementor(start));
	}
	
	private static <V extends GroupAdditiveElement<V> & Comparable> GroupAdditiveIncrementor<V> getIncrementor(V start){
		if (start instanceof RingElement){
			return new GroupAdditiveIncrementor<V>((V) ((RingElement)start).getAlgebricStructure().one());
		}
		return new GroupAdditiveIncrementor<V>(start.getAlgebricStructure().zero());
	}

	/**
	 * @return
	 */
	protected boolean isStepDefined() {
		return !((GroupAdditiveIncrementor)this.incrementor).getStep().isZero();
	}

}
