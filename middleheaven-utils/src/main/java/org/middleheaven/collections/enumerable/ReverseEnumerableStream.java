/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 */
class ReverseEnumerableStream<T> extends AbstractEnumerable<T>  {

	private final Deque<T> deque;
	
	/**
	 * Constructor.
	 * @param enumerable
	 * @param predicate
	 */
	public ReverseEnumerableStream(Enumerable<T> enumerable) {
		this.deque = new LinkedList<T>();
		for (T item : enumerable){
			deque.add(item);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return deque.descendingIterator();
	}


}
