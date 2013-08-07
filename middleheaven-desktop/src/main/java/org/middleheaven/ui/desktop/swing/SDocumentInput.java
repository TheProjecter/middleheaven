package org.middleheaven.ui.desktop.swing;

import java.io.Serializable;

import javax.swing.BoxLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.data.UIDataContainer;

public abstract class SDocumentInput extends SBaseFieldInput implements UIField{
	

	private static final long serialVersionUID = -1521785089966526395L;


	private final JTextComponent txtField;

	public SDocumentInput(JTextComponent txtField){
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.txtField = txtField;
		this.add(txtField);
		
		Document document = getDocument(this.txtField.getDocument());
		
		// if is not the same
		if (txtField.getDocument() != document){
			// change
			txtField.setDocument(document);
		}
		
		txtField.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent event) {
				getValueProperty().set(format(SDocumentInput.this.txtField.getText()));
			}

			@Override
			public void insertUpdate(DocumentEvent event) {
				getValueProperty().set(format(SDocumentInput.this.txtField.getText()));
			}

			@Override
			public void removeUpdate(DocumentEvent event) {
				getValueProperty().set(format(SDocumentInput.this.txtField.getText()));
			}
			
		});
	}
	
	protected void setupTextField(JTextComponent text){
		
	}
	
	public  Document getDocument(Document currentDocument){
		return currentDocument;
	}
	
	protected Serializable format(String raw){
		return  raw;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIDataContainer(UIDataContainer container) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	@Override
	public boolean hasFocus() {
		return txtField.hasFocus();
	}


	
	
}
