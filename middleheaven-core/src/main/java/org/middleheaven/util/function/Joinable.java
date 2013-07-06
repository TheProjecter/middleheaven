/**
 * 
 */
package org.middleheaven.util.function;

/**
 * 
 */
public interface Joinable<F, T> extends Functor<F, T> {

	public Functor<?, F> join();

}
