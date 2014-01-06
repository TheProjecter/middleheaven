/**
 * 
 */
package org.middleheaven.util;

/**
 * A type with the concept of next and previous.
 */
public interface NaturalIncrementable<I> {

	/**
	 * The value next to this.
	 * @return  The value next to this.
	 */
	public I next();
	
	/**
	 * The value previous to this.
	 * @return  The value previous to this.
	 */
	public I previous();
}
