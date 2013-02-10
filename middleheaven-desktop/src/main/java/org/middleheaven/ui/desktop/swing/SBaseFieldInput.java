package org.middleheaven.ui.desktop.swing;

import javax.swing.BoxLayout;

import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

public abstract class SBaseFieldInput extends SBaseInput implements UIField {
	

	private static final long serialVersionUID = 1L;

	
	public SBaseFieldInput(){
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	}
	
	private Property<Boolean> required = ValueProperty.writable("required", Boolean.class);
	private Property<Integer> maxLength= ValueProperty.writable("maxLength", Integer.class);
	private Property<Integer> minLength= ValueProperty.writable("minLength", Integer.class);
	private Property<Object> value= ValueProperty.writable("value", Object.class);
	private Property<ParsableFormatter> formatter = ValueProperty.writable("formatter", ParsableFormatter.class);
	
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
	public Property<Object> getValueProperty(){
		return value;
	}
	
}
