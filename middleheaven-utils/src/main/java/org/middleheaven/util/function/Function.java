/**
 * 
 */
package org.middleheaven.util.function;

/**
 * Generic function object of one argument.
 */
public interface Function<R, T> {

	/**
	 * Apply the function to the given object
	 * @param object the given object.
	 * @return the result of the function.
	 */
	public R apply(T object);
}
