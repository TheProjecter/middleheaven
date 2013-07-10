package org.middleheaven.ui.web.tags;



public class EmailInputTag extends TextInputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5638167256857318728L;

	public EmailInputTag() {
		this.setMaxLength(255);
		this.setSubtype("email");
	}

}
