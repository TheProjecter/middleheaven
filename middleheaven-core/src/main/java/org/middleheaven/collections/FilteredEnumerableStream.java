/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class FilteredEnumerableStream<T> extends AbstractEnumerable<T> {

	private Enumerable<T> enumerable;
	private Predicate<T> predicate;

	/**
	 * Constructor.
	 * @param enumerable
	 * @param predicate
	 */
	public FilteredEnumerableStream(Enumerable<T> enumerable,Predicate<T> predicate) {
		this.enumerable = enumerable;
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new FilteredIterator<T>(enumerable.iterator(), predicate);
	}


	



}
