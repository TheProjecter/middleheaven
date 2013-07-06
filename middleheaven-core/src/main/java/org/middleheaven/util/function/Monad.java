/**
 * 
 */
package org.middleheaven.util.function;

public abstract class Monad<M, T> implements Joinable<M, T> {
	
	public <U> Monad<M, U> bind(Function<Monad<M, U>, T> f) {
		Function<Monad<M, Monad<M, U>>, Monad<M, T> > a = (Function<Monad<M, Monad<M, U>>, Monad<M, T> >) fmap(f);
		Monad<M, Monad<M, U>> mmonad = a.apply(this);
		return (Monad<M, U>) mmonad.join();
	}


	public Monad<M, T> fail(String ex) { 
		throw new RuntimeException(ex);
	} 
	
}
