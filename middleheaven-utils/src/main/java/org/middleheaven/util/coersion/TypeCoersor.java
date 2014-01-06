package org.middleheaven.util.coersion;

/**
 * Converts a value of a certain type <code>O</code> into a value of type <code>R</code>
 * 
 * @param <O> original type
 * @param <R> result type
 */
public interface TypeCoersor<O,R> {

	 public <T extends R> T coerceForward(O value, Class<T> type);
	 public <T extends O> T coerceReverse(R value , Class<T> type);
	 public TypeCoersor<R,O> inverse();
	 
}
