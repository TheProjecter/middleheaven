/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UILayoutManager;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * 
 */
public abstract class VaadinUIComponentContainer extends VaadinUIComponent implements UIContainer{

	
	private List<VaadinUIComponent> components = new CopyOnWriteArrayList<VaadinUIComponent>();
	protected UILayout layout;
	
	/**
	 * Constructor.
	 * @param component
	 */
	public VaadinUIComponentContainer(Component component, Class<? extends UIComponent> type) {
		super(component, type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.<UIComponent>unmodifiableList(components);
	}

	protected void addWrapperComponent(VaadinUIComponent c ){
		this.components.add(c);
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	public ComponentContainer getComponent(){
		return (ComponentContainer) super.getComponent();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount() {
		return components.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component) {
		
		if (component instanceof UILayout){
			this.setUIContainerLayout(UILayout.class.cast(component));
			this.components.add((VaadinUIComponent) component);
		} else if ( this.layout == null){
			VaadinUIComponent c = VaadinUIComponent.class.cast(component);
			this.addWrapperComponent(c);
			this.getComponent().addComponent(c.getComponent());
		} else {
			addComponent(component, null);
		}
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component,UILayoutConstraint layoutConstrain) {
		
		VaadinUIComponent c = (VaadinUIComponent)component;
		this.addWrapperComponent(c);
		
		this.layout.addComponent(component, layoutConstrain);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponent(UIComponent component) {
		
		VaadinUIComponent c = (VaadinUIComponent)component;
		components.remove(c);
		
		this.getComponent().addComponent(c.getComponent());
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayout getUIContainerLayout() {
		return layout;
	}





}
