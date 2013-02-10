package org.middleheaven.ui.components;

import java.util.List;

import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.ui.data.UIDataItem;
import org.middleheaven.util.property.Property;


public interface UISelectOne extends UIField {

	public UIDataItem getElementAt(int index);

	/**
	 * 
	 * @return The number of elements in the model
	 */
	public int getDataSize();

	public void clearSelection();

	public boolean isSelectedIndex(int index);

	public boolean isSelectionEmpty();

	public int getMaxSelectionIndex();

	public int getMinSelectionIndex();

	public void setSelectionInterval(int start, int end);

	public void removeSelectionInterval(int start, int end);

	/**
	 * In the order of selection index.
	 * @return
	 */
	public List<UIDataItem> getSelected();
	
	/**
	 * 
	 * @param anItem the index of the item in the model or -1 if the item is not in the model
	 * @return
	 */
	public int indexOf(UIDataItem anItem);
}
