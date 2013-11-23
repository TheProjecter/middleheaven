/**
 * 
 */
package org.middleheaven.ui.data;

/**
 * 
 */
public class ListUIDataItem implements UIDataItem {

	private Object[] params;
	public ListUIDataItem(Object ... params){
		this.params= params;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(int i) {
		return params[i];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return params.length;
	}

}
