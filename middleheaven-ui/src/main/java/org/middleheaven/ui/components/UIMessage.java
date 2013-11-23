package org.middleheaven.ui.components;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.property.Property;

public interface UIMessage extends UIOutput {

	public Property<LocalizableText> getTextProperty();
	
}
