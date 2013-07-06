package org.middleheaven.collections;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class EncancedListAdapter<T> extends EnhancedCollectionAdapter<T> implements List<T> {

	public EncancedListAdapter(List<T> original) {
		super(original);
	}
	
	protected List<T> original(){
		return (List<T>) super.original();
	}

	@Override
	public List<T> subList(int a, int b) {
		return new EncancedListAdapter<T>(original().subList(a, b));
	}

	@Override
	public void add(int index, T element) {
		original().add(index , element);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return original().addAll(index , c);
	}

	@Override
	public T get(int index) {
		return original().get(index);
	}

	@Override
	public int indexOf(Object o) {
		return original().indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return original().lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return original().listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return original().listIterator(index);
	}

	@Override
	public T remove(int index) {
		return original().remove(index);
	}

	@Override
	public T set(int index, T element) {
		return original().set(index, element);
	}
	

	@Override
	public T getFirst() {
		return original().isEmpty() ? null : original().get(0);
	}
}
