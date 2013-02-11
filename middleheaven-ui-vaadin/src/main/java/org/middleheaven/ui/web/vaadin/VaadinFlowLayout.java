/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;

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
		this.addWrapperComponent(safeCast(component, VaadinUIComponent.class).get());
	}


	

	
}
