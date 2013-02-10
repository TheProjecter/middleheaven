package org.middleheaven.ui.components;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.util.property.Property;

public interface UIMessage extends UIOutput {

	public Property<TextLocalizable> getTextProperty();
	
}
