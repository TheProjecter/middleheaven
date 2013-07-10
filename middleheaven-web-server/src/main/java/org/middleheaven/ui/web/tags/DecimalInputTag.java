package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;




public class DecimalInputTag extends InputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9166361621174919719L;

	public DecimalInputTag() {
		super("TEXTFIELD");
		this.setSubtype("number");
	}
	
	protected void writeInputOption() throws JspTagException {
		
		// sinals the form that this is a decimal field capable form
		FormTag form = this.findAncestorTag(FormTag.class);
		form.setHasDecimalFields(true);
		
		super.writeInputOption();

	}
}
