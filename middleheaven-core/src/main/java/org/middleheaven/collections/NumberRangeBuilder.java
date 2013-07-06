/**
 * 
 */
package org.middleheaven.collections;

import org.middleheaven.util.Incrementor;
import org.middleheaven.util.NumberIncrementor;
import org.middleheaven.util.coersion.TypeCoercing;

/**
 * 
 */
public class NumberRangeBuilder<T extends Number & Comparable, I extends Number & Comparable> extends RangeBuilder<T, I>{


	/**
	 * Constructor.
	 * @param start
	 * @param incrementor
	 */
	@SuppressWarnings("unchecked")
	NumberRangeBuilder(T start) {
		super(start, (Incrementor<T, I>) new NumberIncrementor(TypeCoercing.coerce(Integer.valueOf(1), start.getClass())));
	}


	
}
