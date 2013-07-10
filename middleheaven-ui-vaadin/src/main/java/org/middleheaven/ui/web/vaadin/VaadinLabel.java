/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

import com.vaadin.ui.Label;

/**
 * 
 */
public class VaadinLabel extends VaadinOutputUIComponent implements UILabel {

	private Property<LocalizableText> text = ValueProperty.writable("text", LocalizableText.class);
	
	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinLabel() {
		super(new Label(), UILabel.class);
		
		Label component = (Label) this.getComponent();
		component.setContentMode(Label.CONTENT_TEXT);
		
		text.onChange(new Block<LocalizableText>(){

			@Override
			public void apply(LocalizableText text) {
				((Label)getComponent()).setValue(localize(text));
			}
			
		});
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<LocalizableText> getTextProperty() {
		return text;
	}

}
