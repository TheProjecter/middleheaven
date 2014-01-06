/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;

import org.middleheaven.collections.TransformedIterator;
import org.middleheaven.util.function.Function;

/**
 * 
 */
class TransformedIterable<T, C> implements Iterable<C> {

	private Iterable<T> iterable;
	private Function<C, T> classifier;

	/**
	 * Constructor.
	 * @param iterable
	 * @param classifier
	 */
	public TransformedIterable(Iterable<T> iterable, Function<C, T> classifier) {
		this.iterable = iterable;
		this.classifier = classifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<C> iterator() {
		return TransformedIterator.<C,T>transform(iterable.iterator(), classifier);
	}

}
