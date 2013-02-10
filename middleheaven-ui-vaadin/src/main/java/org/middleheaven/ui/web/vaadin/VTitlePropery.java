/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

/**
 * 
 */
public class VTitlePropery extends ValueProperty<TextLocalizable> {

	
	public static Property<TextLocalizable> bind( final VaadinUIComponentContainer component){
		return new VTitlePropery().onChange(new Block<TextLocalizable>(){

			@Override
			public void apply(TextLocalizable text) {
				component.getComponent().setCaption(component.localize(text));
			}
			
		});
	}
	
	/**
	 * Constructor.
	 * @param propertyName
	 * @param value
	 * @param readOnly
	 */
	private VTitlePropery() {
		super("title", null, TextLocalizable.class , false);
	}

}
