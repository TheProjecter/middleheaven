/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UILayoutManager;
import org.middleheaven.ui.layout.UIBorderLayoutManager;
import org.middleheaven.ui.layout.UIBorderLayoutConstraint;
import org.middleheaven.ui.web.vaadin.BorderLayout.Constraint;

/**
 * 
 */
class VaadinBorderLayout extends VaadinUILayout {

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinBorderLayout() {
		super(new BorderLayout());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component) {
		
//		final UIBorderLayout model = (UIBorderLayout) this.getUIModel();
//	
//		addComponent(component, model.getBorderConstraintFor(component));
//	
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstrain) {
		
		
		VaadinUIComponent c = (VaadinUIComponent)component;
		
		UIBorderLayoutConstraint borderConstraint = (UIBorderLayoutConstraint)layoutConstrain;
		
		BorderLayout layout = (BorderLayout) this.getComponent();
		
		switch (borderConstraint){
		case CENTER:
			layout.addComponent(c.getComponent(), Constraint.CENTER);
			break;
		case EAST:
			layout.addComponent(c.getComponent(), Constraint.EAST);
			break;
		case NORTH:
			layout.addComponent(c.getComponent(), Constraint.NORTH);
			break;
		case SOUTH:
			layout.addComponent(c.getComponent(), Constraint.SOUTH);
			break;
		case WEST:
			layout.addComponent(c.getComponent(), Constraint.WEST);
			break;
		}
		
		this.addWrapperComponent(c);
		
	}



}
