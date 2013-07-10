package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;


public class TextInputTag extends InputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7804669772732682756L;


	public TextInputTag() {
		super("TEXTFIELD");
	}
	
	private Integer minLength;
	private Integer maxLength;
	

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}


	protected void writeInputOption() throws JspTagException {
		if (maxLength!=null){
			writeAttribute("maxlength", maxLength);
		}
		
		if (minLength!=null){
			writeAttribute("minlength", minLength);
		}
	}

}
