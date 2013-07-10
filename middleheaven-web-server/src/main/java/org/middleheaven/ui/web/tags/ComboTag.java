package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class ComboTag extends AbstractBodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1407950868263612170L;
	Iterator<CheckableListItem> iterator;
	private String varName = "item"; 
	private String name;
	private String onchange;
	private boolean required =false;
	private boolean enabled = true;
	Iterable<CheckableListItem> iterable;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getName() {
		return name;
	}

	public String getOnchange() {
		return onchange;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVar(String varName){
		this.varName = varName;
	}

	public String getVar(){
		return varName;
	}

	public void setItems(Iterable<CheckableListItem> iterable){
		this.iterable = iterable;
		this.iterator = iterable != null ?  iterable.iterator() : null;
	}

	private CheckableListItem getChecked(){
		if (iterable!=null){
			for (CheckableListItem item : iterable){
				if (item.isCheck()){
					return item;
				}
			}
			return new CheckableListItem("","");
		} else {
			return new CheckableListItem("","");
		}
	}
	public int doStartTag() throws JspException{

		if (enabled){ 
			write("<select ");
			writeAttribute("id",getId()==null ? name : getId());
			writeAttribute("name", name);
			if(onchange != null) {
				writeAttribute("onchange", onchange);
			}
			writeProperties();
			writeLine(">");

			if (iterator!=null && iterator.hasNext()){
				return EVAL_BODY_BUFFERED;
			}

		} else { // write hidden

			write("<input ");
			writeAttribute("type","TEXT");

			writeAttribute("id",getId()==null ? name : getId());
			writeAttribute("disabled","disabled");
			writeAttribute("value",getChecked().getValue());
			writeLine(" />");

			write("<input ");
			writeAttribute("type","HIDDEN");
			writeAttribute("name",name);
			writeAttribute("value",getChecked().getId());
			writeLine(" />");
		}

		return SKIP_BODY;
	}

	protected void writeProperties() throws JspTagException {
		String classes = "field";
		if (required){
			classes += " required";
		}
		writeAttribute("class",classes);
	}

	public void doInitBody (){
		Object obj = iterator.next();
		pageContext.setAttribute(varName, obj);
	}

	public int doAfterBody() throws JspException{

		try {
			getBodyContent().writeOut(getPreviousOut());
			getBodyContent().clear();


			if (iterator.hasNext()){
				Object obj = iterator.next();
				pageContext.setAttribute(varName, obj);
				return EVAL_BODY_BUFFERED;
			}

			return SKIP_BODY;
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}

	}

	public int doEndTag() throws JspTagException {
		write("</select>");
		return EVAL_PAGE;
	}

	public void release(){
		this.iterator = null;
	}

	@Override
	public void releaseState() {
		// TODO implement AbstractBodyTagSupport.releaseState
		
	}
}
