/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.components.UITextField;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;

/**
 * 
 */
public class VaadinTextInput extends VaadinFieldUIComponent implements UITextField {

	/**
	 * Constructor.
	 * @param component 
	 * @param component
	 * @param type
	 */
	public VaadinTextInput(TextField component) {
		super(component, UITextField.class);
		
		
		component.addListener(new TextChangeListener(){

			private static final long serialVersionUID = -3590065398661472455L;

			@Override
			public void textChange(TextChangeEvent event) {
				VaadinTextInput.this.getValueProperty().set(event.getText());
			}
			
		});
		
	}

	
}
