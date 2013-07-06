/**
 * 
 */
package org.middleheaven.util.function;

/**
 * 
 */
public interface Functor<F , T>{

	public <U> Function<? extends Functor<F, U>, ? extends Functor<F, T>> fmap(Function<U,T> f);

	public T arg();

}
