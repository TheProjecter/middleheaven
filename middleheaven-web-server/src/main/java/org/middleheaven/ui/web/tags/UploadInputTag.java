package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspTagException;


public class UploadInputTag extends InputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5675673204375724431L;

	public UploadInputTag() {
		super("FILE");
	}

	protected void writeInputOption() throws JspTagException {

		// signals this is a upload enabled form
		FormTag form = this.findAncestorTag(FormTag.class);
		form.isUpload= true;
	}

}
