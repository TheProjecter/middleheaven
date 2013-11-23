package org.middleheaven.ui.components;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.property.Property;

public interface UIOutput extends UIComponent {

	/**
	 * 
	 * @return
	 */
	public Property<UIReadState> getReadStateProperty();


}
