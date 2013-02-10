package org.middleheaven.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

public abstract class AbstractUIClient implements UIClient , NamingContainer {

	private String family;
	private String id;
	private Map<String, UIComponent> components = new LinkedHashMap<String, UIComponent>();

	private final Property<Boolean> visible = ValueProperty.writable("visible", true);
	private final Property<Boolean> enable = ValueProperty.writable("enable", true);

	public AbstractUIClient(){
		visible.set(true);
		enable.set(true);

	}

	public Property<Boolean> getEnableProperty(){
		return enable;
	}
	
	public Property<Boolean> getVisibleProperty(){
		return visible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}

	public UIComponent findContainedComponent(String componentID){
		return components.get(componentID);
	}

	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		components.put(component.getGID(), component);
	}

	@Override
	public void removeComponent(UIComponent component) {
		components.remove(component.getGID());
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return new ArrayList<UIComponent>(this.components.values());
	}

	@Override
	public int getChildrenCount() {
		return components.size();
	}

	@Override
	public String getFamily() {
		return family;
	}

	@Override
	public String getGID() {
		return id;
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIClient.class;
	}


	@Override
	public final UIComponent getUIParent() {
		return null;
	}

	@Override
	public boolean isRendered() {
		return true;
	}

	@Override
	public void setFamily(String family) {
		this.family = family;
	}

	@Override
	public void setGID(String id) {
		this.id = id;
	}



	@Override
	public void setUIParent(UIComponent parent) {
		//no-op
	}

	@Override
	public void setDisplayableSize(UISize size) {
		throw new UnsupportedOperationException("Can not change " + this.getClass().getSimpleName() + " size");
	}



	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(0,0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSplashWindowUsed() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent resolveMainWindow(UIClient client,
			AttributeContext context) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent resolveSplashWindow(UIClient client,
			AttributeContext context) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout component) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayout getUIContainerLayout() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component,
			UILayoutConstraint layoutConstrain) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

}
