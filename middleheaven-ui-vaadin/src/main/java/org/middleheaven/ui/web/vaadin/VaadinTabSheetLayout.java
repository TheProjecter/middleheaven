/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;

import com.vaadin.ui.TabSheet;

/**
 * 
 */
public class VaadinTabSheetLayout extends VaadinUILayout  {

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinTabSheetLayout() {
		super(new TabSheet());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component,UILayoutConstraint layoutConstrain) {
		
		VaadinUIComponent c = (VaadinUIComponent)component;
		
		
		TabSheet t = (TabSheet) this.getComponent();

		t.addTab(c.getComponent(), c.getComponent().getCaption());
		
		
	}


}
