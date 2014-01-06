/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class DistinctPredicate<T> implements Predicate<T> {

	private Set<T> set = new HashSet<T>();
	
	public DistinctPredicate(){
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean apply(T obj) {
		return set.add(obj);
	}

}
