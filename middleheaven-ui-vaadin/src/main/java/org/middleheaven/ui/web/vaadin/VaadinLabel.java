/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

import com.vaadin.ui.Label;

/**
 * 
 */
public class VaadinLabel extends VaadinOutputUIComponent implements UILabel {

	private Property<TextLocalizable> text = ValueProperty.writable("text", TextLocalizable.class);
	
	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinLabel() {
		super(new Label(), UILabel.class);
		
		Label component = (Label) this.getComponent();
		component.setContentMode(Label.CONTENT_TEXT);
		
		text.onChange(new Block<TextLocalizable>(){

			@Override
			public void apply(TextLocalizable text) {
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
	public Property<TextLocalizable> getTextProperty() {
		return text;
	}

}
