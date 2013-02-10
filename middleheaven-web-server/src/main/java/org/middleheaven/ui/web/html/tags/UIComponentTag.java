package org.middleheaven.ui.web.html.tags;

import org.middleheaven.ui.UIComponent;

public interface UIComponentTag {

	
	public void setEnabled(boolean enabled);
	public Class<? extends UIComponent> getComponentType();
	
	public UIComponent getUIComponent();
}
