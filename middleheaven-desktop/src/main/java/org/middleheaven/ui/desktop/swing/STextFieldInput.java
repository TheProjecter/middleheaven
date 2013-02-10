package org.middleheaven.ui.desktop.swing;

import javax.swing.JFormattedTextField;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UILayoutManager;
import org.middleheaven.ui.components.UITextField;
import org.middleheaven.ui.data.UIDataContainer;

public class STextFieldInput extends SDocumentInput implements UITextField {

	private static final long serialVersionUID = -8779476270509974866L;
	
	
	
	public STextFieldInput(){
		super(new JFormattedTextField());
	}
	
	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UITextField.class;
	}

	






}
