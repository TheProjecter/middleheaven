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
class TransformedEnumerableStream<C, T> extends AbstractEnumerable<C> {

	private Enumerable<T> enumerable;
	private Function<C, T> function;

	/**
	 * Constructor.
	 * @param enumerable
	 * @param function
	 */
	public TransformedEnumerableStream(Enumerable<T> enumerable, Function<C, T> function) {
		this.enumerable = enumerable;
		this.function = function;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<C> iterator() {
		return  TransformedIterator.<C,T>transform(enumerable.iterator(), function);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isFastCount() {
		return enumerable instanceof FastCountEnumerable;
	}
}
