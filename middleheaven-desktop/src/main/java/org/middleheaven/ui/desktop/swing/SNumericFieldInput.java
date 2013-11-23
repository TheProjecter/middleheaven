package org.middleheaven.ui.desktop.swing;

import java.awt.ComponentOrientation;

import javax.swing.JFormattedTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UINumericField;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;

public class SNumericFieldInput extends SDocumentInput implements UINumericField {

	private static final long serialVersionUID = -8779476270509974866L;

	private Property<Integer> decimalDigits = ValueProperty.writable("decimalDigits", 0);

	public SNumericFieldInput(){
		super(new JFormattedTextField());

	}

	protected void setupTextField(JTextComponent text){
		text.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UINumericField.class;
	}

	public Document getDocument(Document current){
		boolean isDecimal = false;

		Integer a = this.decimalDigits.get();
		if (a != null){
			isDecimal = a.compareTo(0)>0;
		}

		((AbstractDocument)current).setDocumentFilter(new NumericTextFilter(isDecimal));
		return current;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Integer> getDecimalDigitsProperty() {
		return decimalDigits;
	}



}
