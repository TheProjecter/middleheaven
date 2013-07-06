/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

/**
 * 
 */
public class ArrayEnumerable<T> extends AbstractEnumerable<T> {

	
	private T[] array;

	public ArrayEnumerable (T[] array){
		this.array = CollectionUtils.duplicateArray(array);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator<T>(array);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getFirst() {
		return array.length == 0 ? null : array[0];
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getLast() {
		return array.length == 0 ? null : array[array.length - 1];
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] intoArray(T[] array) {
		return this.array;
	}
}
