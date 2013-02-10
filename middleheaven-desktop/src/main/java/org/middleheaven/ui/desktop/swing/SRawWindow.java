package org.middleheaven.ui.desktop.swing;

import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JWindow;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIPrespectiveListener;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.components.UIWindowsListener;
import org.middleheaven.util.collections.DelegatingList;
import org.middleheaven.util.property.BindedProperty;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

public class SRawWindow extends JWindow implements UIWindow{


	private static final long serialVersionUID = -3684173828402031480L;
	
	private final Property<Boolean> visible = BindedProperty.bind("visible", this);
	private final Property<Boolean> enable = BindedProperty.bind("enable", this);
	private final Property<TextLocalizable> title = ValueProperty.writable("title", TextLocalizable.class);
	
	
	private String family;
	private String id;
	private UIComponent parent;
	
	public SRawWindow(){
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		this.getContentPane().add((JComponent)component);
		
	}

	@Override
	public void removeComponent(UIComponent component) {
		this.getContentPane().remove((JComponent)component);
	}
	
	@Override
	public List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return (UIComponent)getContentPane().getComponent(index);
			}

			@Override
			public int size() {
				return getComponentCount();
			}
			
		};
	}

	@Override
	public int getChildrenCount() {
		return getComponentCount();
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
		return (Class<T>) UIWindow.class;
	}

	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean isRendered() {
		return true;
	}



	@Override
	public void setFamily(String family) {
		this.family= family;
	}

	@Override
	public void setGID(String id) {
		this.id = id;
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(this.getX(),this.getY());
	}

	@Override
	public UISize getDisplayableSize() {
		return UISize.pixels(this.getWidth(), this.getHeight());
	}

	@Override
	public void setDisplayableSize(UISize size) {
		UISize pixelSize =  SwingUnitConverter.getInstance().toPixels(size, this.getUIParent());

		this.setBounds(
				this.getX(), 
				this.getY(), 
				(int)pixelSize.getWidth().getValue(),
				(int)pixelSize.getHeight().getValue()
		);
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<TextLocalizable> getTitleProperty() {
		return title;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPrespectiveListener(UIPrespectiveListener listener) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePrespectiveListener(UIPrespectiveListener listener) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<UIPrespectiveListener> getPrecpectiveListeners() {
		return Collections.emptySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getVisibleProperty() {
		return visible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getEnableProperty() {
		return enable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addUIWindowListener(UIWindowsListener listener) {
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUIWindowListener(UIWindowsListener listener) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<UIWindowsListener> getUIWindowListeners() {
		return Collections.emptySet();
	}
}
