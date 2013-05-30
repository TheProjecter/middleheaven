package org.middleheaven.ui.desktop.swing;

import java.util.List;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.data.UIDataContainer;
import org.middleheaven.ui.data.UIDataItem;

public class SList extends SBaseFieldInput implements UISelectOne{


	private static final long serialVersionUID = 4655745102374363086L;
	JList list = new JList();

	public SList(){
		this.add(new JScrollPane(list));
	}


	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UISelectOne.class;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIDataItem getElementAt(int index) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearSelection() {
		list.clearSelection();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectedIndex(int index) {
		return list.isSelectedIndex(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionEmpty() {
		return list.isSelectionEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxSelectionIndex() {
		return list.getMaxSelectionIndex();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinSelectionIndex() {
		return list.getMinSelectionIndex();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectionInterval(int start, int end) {
		list.setSelectionInterval(start, end);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSelectionInterval(int start, int end) {
		list.removeSelectionInterval(start, end);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UIDataItem> getSelected() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(UIDataItem anItem) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIDataContainer(UIDataContainer container) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDataSize() {
		return list.getModel().getSize();
	}
}
