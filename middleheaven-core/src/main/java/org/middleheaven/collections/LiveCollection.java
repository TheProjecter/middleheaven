package org.middleheaven.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Permits iteration and manipulation at the same time
 * The iteration continues to the final element, if an element is add 
 * during iteration it will also be iterated
 *
 * @param <T>
 */
public class LiveCollection<T> implements Collection<T> {

	private List<T> all = new ArrayList<T>();
	
	
	public LiveCollection(){}
	
	@Override
	public boolean add(T e) {
		return all.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return all.addAll(c);
	}

	@Override
	public void clear() {
		this.all.clear();
	}

	@Override
	public boolean contains(Object o) {
		return all.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return all.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return all.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return new LiveIterator();
	}
	
	private class LiveIterator implements Iterator<T>{

		private int index=-1;
		
		@Override
		public boolean hasNext() {
			return index<all.size()-1;
		}

		@Override
		public T next() {
			return all.get(++index);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public boolean remove(Object o) {
		return all.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return all.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return all.retainAll(c);
	}

	@Override
	public int size() {
		return all.size();
	}

	@Override
	public Object[] toArray() {
		return all.toArray();
	}

	@Override
	public <X> X[] toArray(X[] a) {
		return all.toArray(a);
	}

	public String toString(){
		return all.toString();
	}
}
