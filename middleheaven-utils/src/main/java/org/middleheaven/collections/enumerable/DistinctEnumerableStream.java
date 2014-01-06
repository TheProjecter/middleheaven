/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;

import org.middleheaven.collections.iterators.FilteredIterator;

/**
 * 
 */
public class DistinctEnumerableStream<T> extends AbstractEnumerable<T> {

	private Enumerable<T> enumerable;

	/**
	 * Constructor.
	 * @param enumerable
	 * @param predicate
	 */
	public DistinctEnumerableStream(Enumerable<T> enumerable) {
		this.enumerable = enumerable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new FilteredIterator<T>(enumerable.iterator(), new DistinctPredicate<T>());
	}


}
