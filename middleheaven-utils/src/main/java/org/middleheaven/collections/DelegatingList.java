package org.middleheaven.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class DelegatingList<E> implements List<E> {

	
	public DelegatingList(){}
	
	@Override
	public abstract int size();
	
	@Override
	public abstract E get(int index);
	
	@Override
	public List<E> subList(int arg0, int arg1) {
		throw new UnsupportedOperationException("SubList is not supported");
	}

	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray() {
		Object[] array = new Object[this.size()];
		
		for (int i = 0; i < array.length ; i++){
			array[i] =  get(i);
		}
		
		return (E[]) array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] array) {
		
		for (int i = 0; i < array.length ; i++){
			array[i] = (T) get(i);
		}
		
		return array;
	}
	
	@Override
	public int indexOf(Object obj) {
		final int size = size();
		for (int i = 0; i < size ; i++){
			if (get(i).equals(obj)){
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int lastIndexOf(Object obj) {
		for (int i = size()-1; i>=0 ; i--){
			if (get(i).equals(obj)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new DelegatingListIterator<E>(index);
	}
	
	private class DelegatingListIterator<T> implements ListIterator<T>{

		private int index;
		
		public DelegatingListIterator(int start){
			index = start -1;
		}
		
		@Override
		public boolean hasNext() {
			return nextIndex() < size();
		}

		@Override
		public boolean hasPrevious() {
			return previousIndex() < 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			return (T) get(++index);
		}

		@Override
		public int nextIndex() {
			return index + 1;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T previous() {
			return (T) get(--index);
		}

		@Override
		public int previousIndex() {
			return index -1;
		}
		
		@Override
		public void add(T arg0) {
			throw new UnsupportedOperationException("This is a read-only list");
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("This is a read-only list");
		}

		@Override
		public void set(T arg0) {
			throw new UnsupportedOperationException("This is a read-only list");
		}
		
	}
	
	@Override
	public boolean isEmpty() {
		return size()==0;
	}

	
	@Override
	public boolean contains(Object obj) {
		return indexOf(obj)>=0;
	}

	@Override
	public boolean containsAll(Collection<?> other) {
		for (Object o : other){
			if (indexOf(o)<0){
				return false;
			}
		}
		return true;
	}

	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public boolean add(E arg0) {
		throw new UnsupportedOperationException("This is a read-only list");
	}

	@Override
	public void add(int arg0, E arg1) {
		throw new UnsupportedOperationException("This is a read-only list");
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		throw new UnsupportedOperationException("This is a read-only list");
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		throw new UnsupportedOperationException("This is a read-only list");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("This is a read-only list");
	}


	@Override
	public Iterator<E> iterator() {
		return listIterator();
	}

	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException("This is a read-only list");
	}

	@Override
	public E remove(int arg0) {
		throw new UnsupportedOperationException("This is a read-only list");
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException("This is a read-only list");
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException("This is a read-only list");
	}

	@Override
	public E set(int arg0, E arg1) {
		throw new UnsupportedOperationException("This is a read-only list");
	}



}
