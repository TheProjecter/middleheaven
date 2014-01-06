/**
 * 
 */
package org.middleheaven.collections.enumerable;

/**
 * 
 */
public class RangeEnumerable extends AbstractIndexableEnumerable<Integer> {

	
	private int start;
	private int end;

	public RangeEnumerable(int start, int end ){
		this.start = start;
		this.end = end;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return end - start + 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getAt(int index) {
		 return start + index;
	}

}
