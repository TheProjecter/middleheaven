package org.middleheaven.ui.components;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;


/**
 * Manages the localization of ist children in a given display area.
 * 
 */
public interface UILayoutManager   {

    public void addComponent (UIComponent component, UILayoutConstraint layoutConstraint);
    public void removeComponent (UIComponent component);
    
}
