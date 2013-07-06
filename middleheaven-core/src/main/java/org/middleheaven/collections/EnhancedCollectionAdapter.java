package org.middleheaven.collections;

import java.util.Collection;
import java.util.Iterator;

/**
 * Allows for a {@link EnhancedCollection} to be create upon a  standard {@link Collection}.
 * @param <T>
 */
class EnhancedCollectionAdapter<T> extends AbstractEnumerableAdapter<T> implements Collection<T>{

	private Collection<T> original;

	protected EnhancedCollectionAdapter(Collection<T> original){
		this.original = original;
	}

	public String toString(){
		return original.toString();
	}
	
	
	protected Collection<T> original(){
		return original;
	}

	public boolean add(T e) {
		return original.add(e);
	}

	public boolean addAll(Collection<? extends T> c) {
		return original.addAll(c);
	}

	public void clear() {
		original.clear();
	}

	public boolean contains(Object o) {
		return original.contains(o);
	}
	
	public boolean containsAll(Collection<?> c) {
		return original.containsAll(c);
	}

	public boolean isEmpty() {
		return original.isEmpty();
	}


	public Iterator<T> iterator() {
		return original.iterator();
	}

	public boolean remove(Object o) {
		return original.remove(o);
	}


	public boolean removeAll(Collection<?> c) {
		return original.removeAll(c);
	}


	public boolean retainAll(Collection<?> c) {
		return original.retainAll(c);
	}

	@Override
	public int size() {
		return original.size();
	}

	public Object[] toArray() {
		return original.toArray();
	}


	public <O> O[] toArray(O[] a) {
		return original.toArray(a);
	}


}
