package org.middleheaven.ui.components;

import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;



public interface UIContainer extends UIComponent  {

    public void setUIContainerLayout(UILayout component);
    public UILayout getUIContainerLayout();
    
    public void addComponent (UIComponent component, UILayoutConstraint layoutConstrain);
    public void removeComponent (UIComponent component);
    
    /**
     * Add a component
     * @param component component to add
     */
    public void addComponent(UIComponent component);
   
    
	/**
	 * Obtains the children components. This can be implements so the components are retrived dinamicly.
	 * 
	 * @param component the parent component.
	 * 
	 * @return the components children.
	 */
	public List<UIComponent> getChildrenComponents();
	

}
