/**
 * 
 */
package org.middleheaven.collections.iterators;

import java.util.Iterator;

import org.middleheaven.util.function.Predicate;

/**
 * 
 */
class FilteredIterable<T> implements Iterable<T> {

	private Iterable<T> original;
	private Predicate<T> predicate;

	/**
	 * Constructor.
	 * @param iterable
	 * @param predicate
	 */
	public FilteredIterable(Iterable<T> iterable, Predicate<T> predicate) {
		this.original = iterable;
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new FilteredIterator<T>(original.iterator(), predicate);
	}



}
