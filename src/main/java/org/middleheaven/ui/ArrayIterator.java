package org.middleheaven.ui;

import java.util.Iterator;

public final class ArrayIterator<T> implements Iterator<T> {

	T[] array;
	int index = -1;
	
	public ArrayIterator(T[] array) {
		super();
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return index < array.length - 1;
	}

	@Override
	public T next() {
		return array[++index];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
