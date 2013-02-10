package org.middleheaven.ui.components;

import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.util.property.Property;


public interface UINumericField extends UIField{

	public Property<ParsableFormatter> getFormaterProperty();
	public Property<Integer> getDecimalDigitsProperty();
}
