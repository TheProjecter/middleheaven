package org.middleheaven.ui.desktop.swing;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenu;

import org.middleheaven.collections.DelegatingList;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.property.BindedProperty;
import org.middleheaven.ui.property.Property;

public class SMenu extends JMenu implements UICommandSet {

	private static final long serialVersionUID = 1L;
	
	private String family;
	private String id;
	private UIComponent parent;

	private Property<Boolean> visible = BindedProperty.bind("visible", this);
	private Property<Boolean> enabled = BindedProperty.bind("enabled", this);
	private Property<String> name = BindedProperty.bind("name", this);;
	private Property<LocalizableText> text = STextProperty.bind(this);
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
		return enabled;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<LocalizableText> getTextProperty() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<String> getNameProperty() {
		return name;
	}
	
	public SMenu(){

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
		this.add(safeCast(component, JComponent.class).get());
	}

	@Override
	public void removeComponent(UIComponent component) {
		this.remove(safeCast(component, JComponent.class).get());
	}
	
	@Override
	public List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return  safeCast(getComponent(index), UIComponent.class).get();
			}

			@Override
			public int size() {
				return getComponentCount();
			}
			
		};
	}


	@Override
	public int getChildrenCount() {
		return this.getComponentCount();
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
		return (Class<T>) UICommandSet.class;
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
		this.family = family;
	}

	@Override
	public void setGID(String id) {
		this.id= id;
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
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
	
	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(this.getX(),this.getY());
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCommandListener(CommandListener listener) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCommandListener(CommandListener listener) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<CommandListener> getCommandListeners() {
		return Collections.emptySet();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout component) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayout getUIContainerLayout() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component,
			UILayoutConstraint layoutConstrain) {
	}


}
