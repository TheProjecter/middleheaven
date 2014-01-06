/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;


/**
 * 
 */
class SkipEnumerable<T> extends AbstractEnumerable<T> {

	private int skip;
	private Enumerable<T> original;
	
	public SkipEnumerable(int skip , Enumerable<T> original){
		this.skip = skip;
		this.original = original;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		Iterator<T> it = original.iterator();
		for(int i=0; i < skip && it.hasNext();i++){
			it.next();
		}
		return it;
	}
	
	@Override
	public int size(){
		return Math.max(0, original.size() - skip);
	}
	
}
