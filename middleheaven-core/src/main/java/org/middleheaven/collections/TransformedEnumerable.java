package org.middleheaven.collections;

import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.util.function.Mapper;

/**
 * Allows type safe transformation from a {@link Enumerable} of objects to another, using a {@link Mapper}.
 * 
 * @param <O> original object type
 * @param <T> target object type
 */
public final class TransformedEnumerable<O,T> extends AbstractEnumerableAdapter<T>{

	private Enumerable<? extends O> original;
	private Mapper<T, O> classifier;
	
	/**
	 * Creates a TransformedEnumerable from the original objects and a classifier.
	 * @param <R> the original object type
	 * @param <M> the target object type
	 * @param original the original enumerable.
	 * @param classifier the classifier that will transform the data
	 * @return the resulting TransformedEnumerable.
	 */
	public static <R,M> TransformedEnumerable<R,M> transform(Enumerable<? extends R> original, Mapper<M,R> classifier){
		return new TransformedEnumerable<R,M>(original, classifier);
	}
	
	/**
	 * Creates a TransformedEnumerable from the original objects and a classifier.
	 * @param <R> the original object type
	 * @param <M> the target object type
	 * @param original the original collection.
	 * @param classifier the classifier that will transform the data
	 * @return the resulting TransformedEnumerable.
	 */
	public static <R,M> TransformedEnumerable<R,M> transform(Collection<? extends R> original, Mapper<M,R> classifier){
		return new TransformedEnumerable<R,M>(CollectionUtils.asEnumerable(original), classifier);
	}
	
	
	private TransformedEnumerable(Enumerable<? extends O> original, Mapper<T,O> classifier){
		this.original = original;
		this.classifier = classifier;
	}
	
	
	private Iterator<? extends O> getOriginalIterator(){
		return original.iterator();
	}
	
	@Override
	public Iterator<T> iterator() {
		final Iterator<? extends O> originalIt = getOriginalIterator();
		return new Iterator <T>(){

			@Override
			public boolean hasNext() {
				return originalIt.hasNext();
			}

			@Override
			public T next() {
				return classifier.apply(originalIt.next());
			}

			@Override
			public void remove() {
				originalIt.remove();
			}
			
		};
	}

	@Override
	public int size() {
		return original.size();
	}

	@Override
	public boolean isEmpty() {
		return original.isEmpty();
	}




}
