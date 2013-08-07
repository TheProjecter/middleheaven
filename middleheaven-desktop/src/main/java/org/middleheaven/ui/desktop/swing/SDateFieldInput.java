package org.middleheaven.ui.desktop.swing;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIDateField;
import org.middleheaven.ui.data.UIDataContainer;

public class SDateFieldInput extends SDocumentInput implements UIDateField {

	private static final long serialVersionUID = -8779476270509974866L;

	public SDateFieldInput(){
		super(new JFormattedTextField());
	
	}
	
	protected void setupTextField(JTextComponent text){
		((JFormattedTextField)text).setFormatterFactory(new AbstractFormatterFactory(){

			@Override
			public AbstractFormatter getFormatter(JFormattedTextField txt) {
				if (getFormaterProperty() != null){
					return new AbstractFormatterAdapter(getFormaterProperty().get());
				} else {
					try {
						return new MaskFormatter("##/##/####");
					} catch (ParseException e) {
						return null;
					}
				}
			}
			
		});
	}
	
	protected Serializable format(String raw){
		
		try {
			return  getFormaterProperty() !=null ? getFormaterProperty().get().format(raw) : new SimpleDateFormat("dd/MM/yyyy").parse(raw);
		} catch (ParseException e) {
			return raw;
		}
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIDateField.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIDataContainer(UIDataContainer container) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	
}
