/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;


/**
 * 
 */
class IterableEnumerable<T> extends AbstractIterableEnumerable<T> {

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
