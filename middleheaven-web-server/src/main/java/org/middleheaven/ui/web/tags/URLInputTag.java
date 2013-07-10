package org.middleheaven.ui.web.tags;



public class URLInputTag extends TextInputTag{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6441962091910537331L;

	public URLInputTag() {
		this.setMaxLength(255);
		this.setSubtype("url");
	}

}
