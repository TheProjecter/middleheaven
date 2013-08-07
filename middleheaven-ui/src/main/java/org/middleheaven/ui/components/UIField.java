package org.middleheaven.ui.components;

import java.io.Serializable;

import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.ui.data.UIDataDisplay;
import org.middleheaven.util.property.Property;


/**
 * Represent a single value user inputed value (normally by diitation)
 */
public interface UIField extends UIInput , UIDataDisplay {

	public Property<ParsableFormatter> getFormaterProperty();
	public Property<Boolean> getRequiredProperty();
	public Property<Integer> getMaxLengthProperty();
	public Property<Integer> getMinLengthProperty();
	public Property<Serializable> getValueProperty();
}
