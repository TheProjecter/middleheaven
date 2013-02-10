/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

import com.vaadin.ui.Component;

/**
 * 
 */
public abstract class VaadinOutputUIComponent extends VaadinUIComponent {

	private Property<UIReadState> readState = ValueProperty.writable("readState", UIReadState.class);
	
	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinOutputUIComponent(Component component, Class<? extends UIComponent> type) {
		super(component, type);
	}

	public Property<UIReadState> getReadStateProperty(){
		return readState;
	}

}
