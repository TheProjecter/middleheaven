package org.middleheaven.ui.components;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.util.property.Property;

public interface UIMessage extends UIOutput {

	public Property<LocalizableText> getTextProperty();
	
}
