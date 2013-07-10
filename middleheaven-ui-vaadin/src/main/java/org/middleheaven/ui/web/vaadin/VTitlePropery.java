/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

/**
 * 
 */
public final class VTitlePropery extends ValueProperty<LocalizableText> {

	
	public static Property<LocalizableText> bind( final VaadinUIComponentContainer component){
		return new VTitlePropery().onChange(new Block<LocalizableText>(){

			@Override
			public void apply(LocalizableText text) {
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
		super("title", null, LocalizableText.class , false);
	}

}
