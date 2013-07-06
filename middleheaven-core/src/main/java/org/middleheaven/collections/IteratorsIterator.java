package org.middleheaven.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorsIterator<T> implements Iterator<T>{

	public static <T> IteratorsIterator<T> aggregateIterables(Iterable<? extends Iterable<T>> collections){
		
		List<Iterator<T>> iterators = new ArrayList<Iterator<T>>();

		for (Iterable<T> c : collections){
			iterators.add(c.iterator());
		}

		return aggregateIterators(iterators);
	}
	
	public static <T> IteratorsIterator<T> aggregateIterators(Iterable<? extends Iterator<T>> collections){
		return new IteratorsIterator<T>(collections);
	}
	
	List<Iterator<T>> iterators;
	int index=0;

	private < E extends Iterator<T>> IteratorsIterator(Iterable<E> iterators) {
		super();
		this.iterators = new ArrayList<Iterator<T>>();

		for (Iterator<T> c : iterators){
			this.iterators.add(c);
		}


	}
	
	@Override
	public boolean hasNext() {
		if (iterators.isEmpty()){
			return false;
		}
		
		while (index < iterators.size()){
			
			if(iterators.get(index).hasNext()){
				return true;
			}
			index++;
		};
		
		index=0;
		return false;
	}

	@Override
	public T next() {
		return iterators.get(index).next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}


}
