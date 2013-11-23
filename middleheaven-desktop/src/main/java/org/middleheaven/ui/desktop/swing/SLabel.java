package org.middleheaven.ui.desktop.swing;

import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.property.BindedProperty;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;

public class SLabel extends JLabel implements UILabel {


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	private static final long serialVersionUID = 1L;
	
	private final Property<Boolean> visible = BindedProperty.bind("visible", this);
	private final Property<Boolean> enable = BindedProperty.bind("enable", this);
	private final Property<LocalizableText> text = STextProperty.bind(this);
	
	private String family;
	private String id;
	private UIComponent parent;

	
	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.emptyList();
	}

	@Override
	public int getChildrenCount() {
		return 0;
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
		return (Class<T>) UILabel.class;
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
	public UISize getDisplayableSize() {
		return UISize.pixels(this.getWidth(), this.getHeight());
	}

	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(this.getX(),this.getY());
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
	public Property<UIReadState> getReadStateProperty() {
		return ValueProperty.readOnly("readState", UIReadState.OUTPUT_ONLY);
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
	

}
