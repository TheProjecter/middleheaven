/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.components.UISecretField;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.PasswordField;

/**
 * 
 */
public class VaadinSecretInput extends VaadinFieldUIComponent implements UISecretField {

	/**
	 * Constructor.
	 * @param component 
	 * @param component
	 * @param type
	 */
	public VaadinSecretInput(PasswordField component) {
		super(component, UISecretField.class);
		
		component.addListener(new TextChangeListener(){

			private static final long serialVersionUID = -3455283661579334779L;

			@Override
			public void textChange(TextChangeEvent event) {
				VaadinSecretInput.this.getValueProperty().set(event.getText());
			}
			
		});
	}


	
}
