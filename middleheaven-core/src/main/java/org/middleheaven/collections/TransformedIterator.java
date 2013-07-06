package org.middleheaven.collections;

import java.util.Iterator;

import org.middleheaven.util.function.Mapper;

/**
 * Adapts an Iterator of type <code>A</code> to an Iterator of type <code>T</code>.
 * 
 * @param <T> the type of objects for the iterator
 * @param <A> the type of object that actually will be iterated.
 */
public final class TransformedIterator<T,A> implements Iterator<T> {

	
	public static <TRANSFORMED, ORIGINAL> TransformedIterator<TRANSFORMED, ORIGINAL> transform(Iterator<ORIGINAL> original, Mapper<TRANSFORMED,ORIGINAL> classifier ){
		return new TransformedIterator<TRANSFORMED, ORIGINAL>(original, classifier);
	}
	
	private Iterator<A> other;
	private Mapper<T, A> classifier;
	
	
	/**
	 * 
	 * Constructor.
	 * @param other the iterator to adapt.
	 */
	public TransformedIterator(Iterator<A> other, Mapper<T,A> classifier){
		this.other = other;
		this.classifier = classifier;
	}
	
	@Override
	public final boolean hasNext() {
		return other.hasNext();
	}

	@Override
	public final T next() {
		return this.classifier.apply(other.next());
	}


	@Override
	public final void remove() {
		other.remove();
	}

	
}
