package org.middleheaven.util;


/**
 * 
 * @param <T> the type of the value
 * @param <I> the type o f the increment on the value
 */
public class EmptyIncrementor<T, I> implements Incrementor<T, I>{

	@SuppressWarnings("rawtypes")
	private static final Incrementor ME = new EmptyIncrementor();
	
	@SuppressWarnings("unchecked")
	public static <L, J> Incrementor<L, J> emptyIncrementor(){
		return ME;
	}
	
	private EmptyIncrementor(){}
	
	@Override
	public T increment(T object) {
		return object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<T, I> reverse() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Incrementor<T, I> withStep(I step) {
		return this;
	}

}
