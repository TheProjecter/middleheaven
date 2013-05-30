package org.middleheaven.ui.desktop.swing;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;

import org.middleheaven.collections.DelegatingList;
import org.middleheaven.ui.NamingContainer;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UILayoutManager;

public class SLayout extends SBasePanel implements NamingContainer , UILayout{


	private static final long serialVersionUID = 7689224212258618032L;
	
	Map<String, UIComponent> childMap = new TreeMap<String, UIComponent>();
	
	UILayoutManager manager;
	
	public void addComponent(UIComponent c){
		c.setUIParent(this);
		this.childMap.put(c.getGID(), c);
		
		if (this.getComponentCount() == 0 || this.getComponent(0) instanceof UIComponent){
			c.setUIParent(this);
			this.add(safeCast(c, JComponent.class).get());
		}
	}
	
	@Override
	public void addComponent(UIComponent component,UILayoutConstraint layoutConstrain) {
		addComponent(component);	
		manager.addComponent(component, layoutConstrain);
	}

	@Override
	public final void removeComponent(UIComponent component) {
		this.remove(safeCast(component, JComponent.class).get());
		manager.removeComponent(component);
	}
	
	@Override
	public final int getChildrenCount() {
		return this.getComponentCount();
	}
	
	@Override
	public List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return safeCast(getComponent(index), UIComponent.class).get();
			}

			@Override
			public int size() {
				return getComponentCount();
			}
			
		};
	}
	@Override
	public UIComponent findContainedComponent(String componentID) {
		return childMap.get(componentID);
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UILayout.class;
	}



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



}
