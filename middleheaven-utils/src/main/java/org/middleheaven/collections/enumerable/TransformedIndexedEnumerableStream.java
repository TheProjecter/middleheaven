/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;

import org.middleheaven.collections.TransformedIndexedIterator;
import org.middleheaven.util.function.BinaryFunction;

/**
 * 
 */
class TransformedIndexedEnumerableStream<C, T> extends AbstractEnumerable<C> {

	private Enumerable<T> enumerable;
	private  BinaryFunction<C, T, Integer> function;

	/**
	 * Constructor.
	 * @param enumerable
	 * @param function
	 */
	public TransformedIndexedEnumerableStream(Enumerable<T> enumerable, BinaryFunction<C, T, Integer> function) {
		this.enumerable = enumerable;
		this.function = function;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<C> iterator() {
		return  TransformedIndexedIterator.<C,T>transform(enumerable.iterator(), function);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isFastCount() {
		return enumerable instanceof FastCountEnumerable;
	}
}
