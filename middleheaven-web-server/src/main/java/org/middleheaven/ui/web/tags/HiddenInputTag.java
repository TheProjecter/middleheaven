package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;

public class HiddenInputTag extends AbstractTagSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2107506358504510362L;


	public HiddenInputTag() {
	}

	private String name;
	private Object value;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int doStartTag() throws JspException {
		write("<input ");
		if (this.getId()!=null){
			writeAttribute("id",getId());
		}
		writeAttribute("type","HIDDEN");
		writeAttribute("name",name);
		writeAttribute("value",value);
		writeLine(" />");
		return SKIP_BODY;
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

}
