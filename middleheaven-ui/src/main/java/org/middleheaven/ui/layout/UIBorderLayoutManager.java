package org.middleheaven.ui.layout;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UILayoutManager;
import org.middleheaven.util.collections.BoundMap;
import org.middleheaven.util.collections.HashBoundMap;

/**
 * Border Layout.
 */
public class UIBorderLayoutManager  implements UILayoutManager {

	// map the component id to its position
	private final BoundMap<String, UIBorderLayoutConstraint> children = new HashBoundMap<String, UIBorderLayoutConstraint>();

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstrain) {
		children.put(component.getGID(), (UIBorderLayoutConstraint)layoutConstrain);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponent(UIComponent component) {
		children.remove(component.getGID());
	}
	

	public UISize getLayoutablePreferedSize(UISize layoutable) {
		return layoutable;
	}

	public UIBorderLayoutConstraint getBorderConstraintFor(UIComponent component){
		return this.children.get(component.getGID());
	}
	
	/**
	 * 
	 * @param contraint
	 * @return the id of the component positioned at the given constraint, or <code>null</code> if no component is at that position
	 */
	public String getBorderComponentId(UIBorderLayoutConstraint contraint){
		return this.children.reversed().get(contraint);
	}


	
	
}
