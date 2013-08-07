/**
 * 
 */
package org.middleheaven.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public final class ArrayIterable<T> implements Iterable<T> {

	private final List<T> list;

	public ArrayIterable(T[] list){
		this.list = Arrays.asList(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator<T>(list);
	}

}
