/**
 * 
 */
package org.middleheaven.ui.components;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;

/**
 * 
 */
public interface UILayout extends UIComponent {


	public void addComponent(UIComponent component,UILayoutConstraint layoutConstrain);

	public void removeComponent(UIComponent component);
	
	public UILayoutManager getLayoutManager();
	public void setLayoutManager(UILayoutManager layoutManager);
}
