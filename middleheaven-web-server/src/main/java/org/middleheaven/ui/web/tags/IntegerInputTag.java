package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;



public class IntegerInputTag extends InputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = 241334132926728062L;

	public IntegerInputTag() {
		super("TEXTFIELD");
		this.setSubtype("digits");
	}
	
	protected void writeInputOption() throws JspTagException {
		
		// signals to the form that this is a integer field enabled form
		FormTag form = this.findAncestorTag(FormTag.class);
		form.setHasIntegerFields(true);
		
		super.writeInputOption();

	}

}
