package org.middleheaven.collections;

import java.util.Iterator;

import org.middleheaven.util.function.BinaryFunction;

/**
 * Adapts an Iterator of type <code>A</code> to an Iterator of type <code>T</code>.
 * 
 * @param <T> the type of objects for the iterator
 * @param <A> the type of object that actually will be iterated.
 */
public final class TransformedIndexedIterator<T,A> implements Iterator<T> {

	
	public static <TRANSFORMED, ORIGINAL> TransformedIndexedIterator<TRANSFORMED, ORIGINAL> transform(Iterator<ORIGINAL> original, BinaryFunction<TRANSFORMED,ORIGINAL, Integer> classifier ){
		return new TransformedIndexedIterator<TRANSFORMED, ORIGINAL>(original, classifier);
	}
	
	private Iterator<A> other;
	private BinaryFunction<T, A, Integer> function;
	private int index = -1;
	
	/**
	 * 
	 * Constructor.
	 * @param other the iterator to adapt.
	 */
	public TransformedIndexedIterator(Iterator<A> other, BinaryFunction<T,A, Integer> function){
		this.other = other;
		this.function = function;
	}
	
	@Override
	public final boolean hasNext() {
		return other.hasNext();
	}

	@Override
	public final T next() {
		index++;
		return this.function.apply(other.next(), Integer.valueOf(index));
	}


	@Override
	public final void remove() {
		other.remove();
	}

	
}
