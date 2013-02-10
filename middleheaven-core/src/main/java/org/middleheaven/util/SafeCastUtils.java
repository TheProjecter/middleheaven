/**
 * 
 */
package org.middleheaven.util;

import org.middleheaven.util.function.Maybe;

/**
 * 
 */
public class SafeCastUtils {

	
	private SafeCastUtils(){}
	
	public static <T> Maybe<T> safeCast(Object any , Class<T> type){
		if (any != null && type.isInstance(any)){
			return Maybe.of(type.cast(any));
		} else {
			return Maybe.absent();
		}
	}
}
