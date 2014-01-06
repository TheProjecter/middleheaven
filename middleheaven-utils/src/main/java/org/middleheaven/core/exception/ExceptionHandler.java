/*
 * Created on 5/Ago/2006
 *
 */
package org.middleheaven.core.exception;

/**
 * Handles exceptions. Handling may produce new exceptions 
 * to be returned 
 * 
 *
 * @param <T> The captured <code>Throwable</code>
 * @param <R> The resulting <code>Throwable</code>, if any.
 */
public interface ExceptionHandler<T extends Throwable, R extends Throwable> {

	/**
	 * 
	 * @param exception the captured exception to handle
	 * @return a new exception or null if none is to be returned
	 */
    public R handle(T exception);
}
