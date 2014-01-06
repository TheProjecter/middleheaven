/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

class SingleIterator<T> implements Iterator<T>{

	T object;
	boolean iterated = false;
	public SingleIterator(T object) {
		this.object = object;
	}

	@Override
	public boolean hasNext() {
		return !iterated;
	}

	@Override
	public T next() {
		iterated =true;
		return object;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}