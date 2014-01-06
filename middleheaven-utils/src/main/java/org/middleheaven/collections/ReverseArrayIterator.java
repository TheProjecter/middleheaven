/**
 * 
 */
package org.middleheaven.collections;

import org.middleheaven.collections.iterators.IndexBasedIterator;

/**
 * 
 */
public class ReverseArrayIterator<T> extends IndexBasedIterator<T> {

	private T[] array;

	/**
	 * Constructor.
	 * @param array
	 */
	public ReverseArrayIterator(T[] array) {
		this.array = array;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int getSize() {
		return array.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected T getObject(int index) {
		return array[array.length - 1 - index];
	}


}
