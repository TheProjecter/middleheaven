/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UILayoutManager;

import com.vaadin.ui.Component;

/**
 * 
 */
public abstract class VaadinUILayout extends VaadinUIComponentContainer implements UILayout {

	
	UILayoutManager manager;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayoutManager getLayoutManager() {
		return manager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLayoutManager(UILayoutManager layoutManager) {
		this.manager = layoutManager;
	}
	
	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinUILayout(Component component) {
		super(component, UILayout.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component) {
		addComponent(component, null);
	
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		this.getComponent().setSizeFull();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout layout) {
		throw new IllegalArgumentException("Caanot set a layout in another layout");
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void addComponent(UIComponent component, UILayoutConstraint layoutConstrain);
}
