package org.middleheaven.collections;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.util.function.Function;

/**
 * Allows type safe transformation from a collection of objects to another, using a {@link Function}.
 * 
 * @param <O> original object type
 * @param <T> target object type
 */
public final class TransformedCollection<O,T> extends AbstractCollection<T> {

	private Collection<? extends O> original;
	private Function<T, O> classifier;
	
	/**
	 * Creates a TransformedCollection from the original objects and a classifier.
	 * @param <ORIGINAL> the original object type
	 * @param <TRANSFORMED> the target object type
	 * @param original the original collection
	 * @param classifier the classifier that will transform the data
	 * @return the resulting TransformedCollection.
	 */
	public static <ORIGINAL,TRANSFORMED> TransformedCollection<ORIGINAL,TRANSFORMED> transform(Collection<? extends ORIGINAL> original, Function<TRANSFORMED,ORIGINAL> classifier){
		return new TransformedCollection<ORIGINAL,TRANSFORMED>(original, classifier);
	}
	
	private TransformedCollection(Collection<? extends O> original, Function<T,O> classifier){
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

}
