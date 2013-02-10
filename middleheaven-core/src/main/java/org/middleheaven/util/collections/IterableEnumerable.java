/**
 * 
 */
package org.middleheaven.util.collections;

import java.util.Iterator;

/**
 * 
 */
public class IterableEnumerable<T> extends AbstractEnumerable<T> {

	
	private Iterable<? extends T> collection;
	
	public IterableEnumerable(Iterable<? extends T> collection){
		this.collection = collection;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) collection.iterator();
	}
	




}
