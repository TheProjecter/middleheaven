/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

/**
 * 
 */
public class ReverseArrayIterable<T> implements Iterable<T> {

	private T[] array;

	/**
	 * Constructor.
	 * @param array
	 */
	public ReverseArrayIterable(T[] array) {
		this.array = array;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new ReverseArrayIterator<T>(array);
	}

}
