/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.data.UIDataItem;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class HtmlUISelectOneImpl extends GenericUIComponent<UISelectOne> implements UISelectOne , HtmlUIComponent {

	private AbstractHtmlRender render;

	private int selectedIndex = -1;
	
	/**
	 * Constructor.
	 * @param component
	 * @param abstractHtmlCommandRender
	 */
	public HtmlUISelectOneImpl(UISelectOne component, AbstractHtmlRender render) {
		super(UISelectOne.class, component.getFamily());
		this.render = render;

		this.getVisibleProperty().set(component.getVisibleProperty().get());
		this.getEnableProperty().set(component.getEnableProperty().get());
		this.getNameProperty().set(component.getNameProperty().get());
		this.getFormaterProperty().set(component.getFormaterProperty().get());
		this.getMaxLengthProperty().set(component.getMaxLengthProperty().get());
		this.getMinLengthProperty().set(component.getMinLengthProperty().get());
		this.getReadStateProperty().set(component.getReadStateProperty().get());
		this.getRequiredProperty().set(component.getRequiredProperty().get());
		this.getValueProperty().set(component.getValueProperty().get());
		this.setUIDataContainer(component.getUIDataContainer());
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeTo(HtmlDocument doc, RenderingContext context)
			throws IOException {
		 this.render.write(doc, context, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIDataItem getElementAt(int index) {
		if (index < 0){
			return null;
		}
		int i = 0;
		for (UIDataItem item : this.getUIDataContainer().getItems()){
			if (i++ == index){
				return item;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDataSize() {
		return this.getUIDataContainer().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearSelection() {
		selectedIndex = -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectedIndex(int index) {
		return index >=0 && selectedIndex == index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionEmpty() {
		return selectedIndex == -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxSelectionIndex() {
		return selectedIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinSelectionIndex() {
		return selectedIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSelectionInterval(int start, int end) {
	    selectedIndex= start;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSelectionInterval(int start, int end) {
		this.clearSelection();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UIDataItem> getSelected() {
		return Collections.singletonList(getElementAt(selectedIndex));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(UIDataItem anItem) {
		if (anItem == null){
			return -1;
		}
		int i = 0;
		for (UIDataItem item : this.getUIDataContainer().getItems()){
			if (item.equals(anItem)){
				return i;
			}
		}
		return -1;
	}
	
	

}
