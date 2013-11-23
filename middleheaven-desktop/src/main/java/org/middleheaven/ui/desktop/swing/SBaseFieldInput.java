package org.middleheaven.ui.desktop.swing;

import java.io.Serializable;

import javax.swing.BoxLayout;

import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.data.UIDataContainer;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;

public abstract class SBaseFieldInput extends SBaseInput implements UIField {
	

	private static final long serialVersionUID = 1L;

	
	public SBaseFieldInput(){
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	}
	
	private Property<Boolean> required = ValueProperty.writable("required", Boolean.class);
	private Property<Integer> maxLength= ValueProperty.writable("maxLength", Integer.class);
	private Property<Integer> minLength= ValueProperty.writable("minLength", Integer.class);
	private Property<Serializable> value= ValueProperty.writable("value", Serializable.class);
	private Property<ParsableFormatter> formatter = ValueProperty.writable("formatter", ParsableFormatter.class);
	private UIDataContainer container;
	
	/**
	 * {@inheritDoc}
	 */
	public Property<ParsableFormatter> getFormaterProperty() {
		return formatter;
	}
	
	public Property<Boolean> getRequiredProperty(){
		return required;
	}
	
	public Property<Integer> getMaxLengthProperty(){
		return maxLength;
	}
	public Property<Integer> getMinLengthProperty(){
		return minLength;
	}
	public Property<Serializable> getValueProperty(){
		return value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIDataContainer(UIDataContainer container) {
		this.container = container;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIDataContainer getUIDataContainer() {
		return container;
	}
	
}
