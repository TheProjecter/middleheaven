/**
 * 
 */
package org.middleheaven.ui.layout;

import org.middleheaven.collections.BoundMap;
import org.middleheaven.collections.HashBoundMap;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.components.UILayoutManager;
import org.middleheaven.util.SafeCastUtils;

/**
 * 
 */
public class UIClientLayoutManager implements UILayoutManager {

	// map the component id to its position
	private final BoundMap<String, UIClientLayoutConstraint> children = new HashBoundMap<String, UIClientLayoutConstraint>();


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstrain) {
		children.put(component.getGID(), SafeCastUtils.safeCast(layoutConstrain, UIClientLayoutConstraint.class).get());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponent(UIComponent component) {
		children.remove(component.getGID());
	}
	
	/**
	 * 
	 * @param contraint
	 * @return the id of the component positioned at the given constraint, or <code>null</code> if no component is at that position
	 */
	public String getComponentId(UIClientLayoutConstraint contraint){
		return this.children.reversed().get(contraint);
	} 

}
