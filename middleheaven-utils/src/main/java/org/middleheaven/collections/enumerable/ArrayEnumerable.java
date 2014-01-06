/**
 * 
 */
package org.middleheaven.collections.enumerable;


/**
 * 
 */
class ArrayEnumerable<T> extends AbstractIndexableEnumerable<T> {

	
	private T[] array;

	public ArrayEnumerable (T[] array){
		this.array = array;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size(){
		return this.array.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getAt(int index) {
		return array[index];
	}

}
