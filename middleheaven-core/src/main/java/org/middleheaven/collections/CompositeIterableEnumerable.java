/**
 * 
 */
package org.middleheaven.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 */
public class CompositeIterableEnumerable<T> extends AbstractEnumerable<T> {

	private List<Iterable<T>> iterables = new ArrayList<Iterable<T>>(3);
	
	public CompositeIterableEnumerable(){
		
	}
	
	public CompositeIterableEnumerable<T> Add(Iterable<T> iterable){
		iterables.add(iterable);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		 return new CompositeIterableEnumerableIterator<T>();
	}
	
	private class CompositeIterableEnumerableIterator<U> implements Iterator<U>{

		private int index = 0;
		private Iterator<U> currentIterator;
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			boolean hasNext = false;
			if (currentIterator != null){
			   hasNext = currentIterator.hasNext();		
			}
			if (!hasNext && iterables.size() > index){
				currentIterator = (Iterator<U>) iterables.get(index++).iterator();
				return hasNext();
			}
			return hasNext;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public U next() {
			return currentIterator.next();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

}
