/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public class FilteredIterator<T> implements Iterator<T> {

	@SuppressWarnings("rawtypes")
	private static final Item EMPTY_ITEM = new Item();
	
	private Predicate<T> predicate;
	private Iterator<T> original;

	private T next;

	private static class Item<T>{
		
		boolean isFound = true;
		T object;
		
		/**
		 * Constructor.
		 * @param n
		 */
		public Item(T n) {
			this.object = n;
			this.isFound = true;
		}
		
		public Item() {
			this.isFound = false;
		}
		
	}
	/**
	 * Constructor.
	 * @param iterator
	 * @param predicate
	 */
	public FilteredIterator(Iterator<T> iterator, Predicate<T> predicate) {
		this.original = iterator;
		this.predicate = predicate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		if (original.hasNext()){
			Item<T> item = fetchNext();
			if (item.isFound){
				this.next = item.object;
				return true;
			} 
		} 
		return false;
	}

	@SuppressWarnings("unchecked")
	private Item<T> fetchNext(){
		while(original.hasNext()){
			T n = original.next();
			if (predicate.apply(n).booleanValue()){
				return new Item<T>(n);
			}
		}
		return EMPTY_ITEM;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next() {
		return next;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		original.remove();
	}

}
