/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

import org.middleheaven.util.function.Mapper;

/**
 * 
 */
public class TransformedEnumerableStream<C, T> extends AbstractEnumerable<C> {

	private Enumerable<T> enumerable;
	private Mapper<C, T> classifier;

	/**
	 * Constructor.
	 * @param enumerable
	 * @param classifier
	 */
	public TransformedEnumerableStream(Enumerable<T> enumerable, Mapper<C, T> classifier) {
		this.enumerable = enumerable;
		this.classifier = classifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<C> iterator() {
		return  TransformedIterator.<C,T>transform(enumerable.iterator(), classifier);
	}



}
