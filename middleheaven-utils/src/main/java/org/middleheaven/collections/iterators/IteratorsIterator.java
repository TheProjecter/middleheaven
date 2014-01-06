package org.middleheaven.collections.iterators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Creates an Iterator<T> over a collection of Iterables<T>.
 * @param <T>
 */
public final class IteratorsIterator<T> implements Iterator<T>{

	public static <T> IteratorsIterator<T> aggregateIterables(Iterator<T> a , Iterator<T> b){
		return new IteratorsIterator<T>(a,b);
	}
	
	public static <T> IteratorsIterator<T> aggregateIterables(Iterator<T> ... iterators){

		Collection<Iterator<T>> iteratorsCollection = new ArrayList<Iterator<T>>(iterators.length);

		for (Iterator<T> it : iterators){
			iteratorsCollection.add(it);
		}

		return aggregateIterators(iteratorsCollection);
	}
	
	public static <T> IteratorsIterator<T> aggregateIterables(Iterable<? extends Iterable<T>> collections){
		
		List<Iterator<T>> iterators = new LinkedList<Iterator<T>>();

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

	private < E extends Iterator<T>> IteratorsIterator(Iterable<E> iterables) {
		super();
		this.iterators = new ArrayList<Iterator<T>>();

		for (Iterator<T> c : iterables){
			this.iterators.add(c);
		}
	}
	
	private  IteratorsIterator(Iterator<T> a , Iterator<T> b) {
		super();
		this.iterators = new ArrayList<Iterator<T>>(2);

		this.iterators.add(a);
		this.iterators.add(b);
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
