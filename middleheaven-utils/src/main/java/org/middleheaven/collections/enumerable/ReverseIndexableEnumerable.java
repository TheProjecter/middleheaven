/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;

import org.middleheaven.collections.iterators.IndexBasedIterator;

/**
 * 
 */
class ReverseIndexableEnumerable<T> extends AbstractIndexableEnumerable<T> {

	private AbstractIndexableEnumerable<T> original;
	
	/**
	 * Constructor.
	 * @param enumerable
	 * @param predicate
	 */
	public ReverseIndexableEnumerable(AbstractIndexableEnumerable<T> original) {
		this.original = original;
	}

	public Enumerable<T> reverse(){
		return original;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return original.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getAt(int index) {
		return original.getAt(size() - 1- index);
	} 

}
