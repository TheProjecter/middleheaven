package org.middleheaven.ui.desktop.swing;

import java.util.List;

import javax.swing.JComboBox;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.data.UIDataContainer;
import org.middleheaven.ui.data.UIDataItem;

public class SDropDownInput extends SBaseFieldInput implements UISelectOne{

	private static final long serialVersionUID = 4655745102374363086L;
	JComboBox combo = new JComboBox();
	
	public SDropDownInput(){
		this.add(combo);
	}
	
	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UISelectOne.class;
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
	public UIDataItem getElementAt(int index) {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDataSize() {
		return combo.getModel().getSize();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearSelection() {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectedIndex(int index) {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionEmpty() {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxSelectionIndex() {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinSelectionIndex() {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectionInterval(int start, int end) {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSelectionInterval(int start, int end) {
		throw new UnsupportedOperationException("Not implememented yet");
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


}
