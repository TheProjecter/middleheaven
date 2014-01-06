/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;

import org.middleheaven.collections.iterators.IndexBasedIterator;

/**
 * 
 */
public abstract class AbstractIndexableEnumerable<T> extends AbstractEnumerable<T> implements FastCountEnumerable{

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isEmpty(){
		return size() == 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T getFirst() {
		return isEmpty() ? null : getAt(0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T getLast() {
		return isEmpty() ? null : getAt(size() - 1);
	}
	
	
	public abstract int size ();
	public abstract T getAt (int index);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new IndexableIterator<T>(this);
	}

	public Enumerable<T> reverse(){
		return new ReverseIndexableEnumerable<T>(this);
	} 

	public static class IndexableIterator<T> extends IndexBasedIterator<T>{

		private AbstractIndexableEnumerable<T> abstractIndexableEnumerable;

		/**
		 * Constructor.
		 * @param abstractIndexableEnumerable
		 */
		public IndexableIterator(AbstractIndexableEnumerable<T> abstractIndexableEnumerable) {
			this.abstractIndexableEnumerable = abstractIndexableEnumerable;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected int getSize() {
			return abstractIndexableEnumerable.size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected T getObject(int index) {
			return abstractIndexableEnumerable.getAt(index);
		}
		
	} 
}
