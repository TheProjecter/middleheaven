/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.List;

/**
 * 
 */
class ListEnumerable<T> extends AbstractIndexableEnumerable<T> implements FastCountEnumerable {

    private List<T> list;

	/**
	 * Constructor.
	 * @param elements
	 */
	public ListEnumerable(List<? extends T> elements) {
		this.list = (List<T>) elements;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int size(){
		return list.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getAt(int index) {
		return list.get(index);
	}

}
