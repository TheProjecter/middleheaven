package org.middleheaven.collections;

import java.util.Iterator;

public abstract class IndexBasedIterator<T> implements Iterator<T>{

	int index=-1;
	public IndexBasedIterator(){}
	
	@Override
	public final boolean hasNext() {
		return index < getSize() - 1;
	}

	@Override
	public final T next() {
		return getObject(++index);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	protected abstract int getSize();
	protected abstract T getObject(int index);

}
