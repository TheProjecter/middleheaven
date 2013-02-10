/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UISize;

import com.vaadin.ui.Component;

/**
 * 
 */
public class VaadinFlowLayout extends VaadinUILayout  {

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinFlowLayout(Component component) {
		super(component);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstrain) {
		final VaadinUIComponent c = (VaadinUIComponent)component;
		this.addWrapperComponent(c);
	}


	

	
}
