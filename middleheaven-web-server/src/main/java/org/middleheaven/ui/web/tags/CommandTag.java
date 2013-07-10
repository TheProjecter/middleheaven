package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.process.ContextScope;

public class CommandTag extends AbstractTagSupport {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3841202315000637651L;
	private String caption;
	private String name;
	private boolean validate= false;
	private boolean isSubmit = true;
	
	public CommandTag() {

	}	

	public void setSubmit(boolean submit){
		this.isSubmit = submit;
	}

	public void setValidate(String validate) {
		this.validate = Boolean.parseBoolean(validate);
	}


	public void setCaption(String caption) {
		this.caption = this.localize(LocalizableText.valueOf(caption), ContextScope.APPLICATION);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int doStartTag() throws JspException {
		write("<input ");
		if (!validate) {
			writeAttribute("class","button cancel");
		} else {
			writeAttribute("class","button");
		}
		if (this.getId() != null) {
			writeAttribute("id",getId());
		}
	
		writeAttribute("type",isSubmit?"submit":"button");
		writeAttribute("name",name);
		writeAttribute("value",caption);
		writeLine(" />");
		return SKIP_BODY;
	}
	

	public int doEndTag() {
		return EVAL_PAGE;
	}

	
}
