package org.middleheaven.collections;

import java.util.Arrays;
import java.util.List;

/**
 * An iterator based on a raw array.
 * @param <T> the array items type.
 */
 final class ArrayIterator<T> extends IndexBasedIterator<T> {

	private List<T> list;

	public ArrayIterator(T[] list) {
		super();
		this.list = Arrays.asList(list);
	}
	
	/**
	 * 
	 * Constructor.
	 * @param array
	 */
	public ArrayIterator(List<T> list) {
		super();
		this.list = list;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected T getObject(int index) {
		return list.get(index);
	}

	@Override
	protected int getSize() {
		return list.size();
	}

}
