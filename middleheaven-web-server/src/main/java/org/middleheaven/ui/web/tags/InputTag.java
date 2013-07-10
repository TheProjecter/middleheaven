package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public abstract class InputTag extends AbstractTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2539647875363641885L;
	private String name;
	private Object value;
	private String type;
	private boolean required = false;
	private boolean enabled = true;
	
	private String subtype = "";
	

	protected String getSubtype() {
		return subtype;
	}

	protected void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public InputTag(String type){
		this.type = type;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
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

	protected String getWritableId(){
		if (id==null){
			return name;
		} 
		return id; 
	}
	
	public int doStartTag() throws JspException {
		write("<input ");
		writeAttribute("type",type);

		writeAttribute("id",getWritableId());
		if (!enabled){
			writeAttribute("disabled","disabled");
		} else {
			writeAttribute("name",name);
		}
		writeAttribute("value",value);
	
		writeInputOption();
		writeProperties();
		writeLine(" />");
		
		if (!enabled){ // write hidden
			write("<input ");
			writeAttribute("type","HIDDEN");
			writeAttribute("name",name);
			writeAttribute("value",value);
			writeLine(" />");
		}
		return SKIP_BODY;
	}
	
	protected void writeProperties() throws JspTagException {
		StringBuilder iClass = new StringBuilder();
		iClass.append("field");
		if (required){
			iClass.append(" required");
		}
		if (!this.subtype.isEmpty()){
			iClass.append(" ").append(subtype);
		}
		
		writeAttribute("class",iClass);
	}

	protected void writeInputOption() throws JspTagException{
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	

	
}
