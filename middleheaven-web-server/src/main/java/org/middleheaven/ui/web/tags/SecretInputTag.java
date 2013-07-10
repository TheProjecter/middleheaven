package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;



public class SecretInputTag extends InputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6558904345748942176L;
	private Integer maxLength;
	
	
	public SecretInputTag() {
		super("PASSWORD");
	}
	
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	
	protected void writeInputOption() throws JspTagException {
		if (maxLength!=null){
			writeAttribute("maxlength", maxLength);
		}

	}
	
}
