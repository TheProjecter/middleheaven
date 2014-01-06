/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Collections;
import java.util.Iterator;

/**
 * 
 */
public class SingleEnumerable<T> extends AbstractEnumerable<T> {

	private T element;
	
	/**
	 * Constructor.
	 * @param element
	 */
	public SingleEnumerable(T element) {
		this.element=  element;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return Collections.singleton(element).iterator();
	}

}
