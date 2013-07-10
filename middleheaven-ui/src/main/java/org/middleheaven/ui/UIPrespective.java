package org.middleheaven.ui;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.util.property.Property;

public interface UIPrespective extends UIContainer {

	public Property<LocalizableText> getTitleProperty();
	

	public void addPrespectiveListener(UIPrespectiveListener listener);
	public void removePrespectiveListener(UIPrespectiveListener listener);
	
	public Iterable<UIPrespectiveListener> getPrecpectiveListeners();
	
}
