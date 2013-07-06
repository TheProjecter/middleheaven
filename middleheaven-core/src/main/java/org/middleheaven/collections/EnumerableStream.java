/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;

/**
 * 
 */
public class EnumerableStream<T> extends AbstractEnumerable<T> {

	
	private Enumerable<T> enumerable;

	public EnumerableStream (Enumerable<T> other){
		this.enumerable = other;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return enumerable.iterator();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.enumerable.size();
	}

	


	



}
