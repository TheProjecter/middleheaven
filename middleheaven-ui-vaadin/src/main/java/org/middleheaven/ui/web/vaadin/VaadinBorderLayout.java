/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.layout.UIBorderLayoutConstraint;
import org.middleheaven.ui.web.vaadin.BorderLayout.Constraint;
import org.middleheaven.util.Maybe;
import org.middleheaven.util.SafeCastUtils;

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
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstraint) {
		
		
		VaadinUIComponent c = safeCast(component, VaadinUIComponent.class).get(); 
		
		Maybe<UIBorderLayoutConstraint> maybeBorderConstraint = SafeCastUtils.safeCast(layoutConstraint, UIBorderLayoutConstraint.class);
		
		if (maybeBorderConstraint.isPresent()){
			
			UIBorderLayoutConstraint borderConstraint = maybeBorderConstraint.get();
			
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
		}
	
		
		this.addWrapperComponent(c);
		
	}



}
