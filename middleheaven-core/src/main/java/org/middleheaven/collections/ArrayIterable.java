/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

/**
 * 
 */
public final class ArrayIterable<T> implements Iterable<T> {

	private final T[] array;

	public ArrayIterable(T[] array){
		this.array = array;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator<T>(array);
	}

}
