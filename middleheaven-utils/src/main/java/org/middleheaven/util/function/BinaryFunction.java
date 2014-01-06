/**
 * 
 */
package org.middleheaven.util.function;

/**
 * Generic function object of two arguments.
 */
public interface BinaryFunction<R, A , B> {

	/**
	 * Applies a function.
	 * @param a the first parameter.
	 * @param b the secound parameter.
	 * @return the result of applying the function.
	 */
	public R apply(A a , B b);
}
