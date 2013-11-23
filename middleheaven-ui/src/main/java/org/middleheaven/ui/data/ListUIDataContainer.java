/**
 * 
 */
package org.middleheaven.ui.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class ListUIDataContainer implements UIDataContainer {

	private List<UIDataItem> items = new ArrayList<UIDataItem>();
	
	public ListUIDataContainer(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<UIDataItem> getItems() {
		return items;
	}
	
	public void add(UIDataItem item){
		items.add(item);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return items.size();
	}

}
