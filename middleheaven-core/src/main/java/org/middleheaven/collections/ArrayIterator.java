package org.middleheaven.collections;

/**
 * An iterator based on a raw array.
 * @param <T> the array items type.
 */
 final class ArrayIterator<T> extends IndexBasedIterator<T> {

	private T[] array;

	/**
	 * 
	 * Constructor.
	 * @param array
	 */
	public ArrayIterator(T[] array) {
		super();
		this.array = CollectionUtils.duplicateArray(array);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected T getObject(int index) {
		return array[index];
	}

	@Override
	protected int getSize() {
		return array.length;
	}

}
