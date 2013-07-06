/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

/**
 * 
 */
public class FlattenEnumerableStream<T> extends AbstractEnumerable<T> {

	private Enumerable<Enumerable<T>>  enumerables;

	/**
	 * Constructor.
	 * @param enumerable
	 * @param classifier
	 */
	public FlattenEnumerableStream(Enumerable<Enumerable<T>> enumerables) {
		this.enumerables = enumerables;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return IteratorsIterator.aggregateIterables(enumerables);
	}



}
