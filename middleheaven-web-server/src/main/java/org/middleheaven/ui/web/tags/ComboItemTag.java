package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class ComboItemTag extends AbstractBodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3988615036886333044L;
	private String value; 
	private boolean isChecked;
	
	public int doStartTag() throws JspException {
		
		// read from list
		ComboTag list = this.findAncestorTag(ComboTag.class);
		if (list!=null){ // list exists
	
			CheckableListItem item = (CheckableListItem)pageContext.getAttribute(list.getVar());
			if (item!=null){
				this.value = item.getId();
				this.isChecked = item.isCheck();
			}
		} // else standalone
		
		write("<option ");
		writeAttribute("value", value);
		if (isChecked){
			writeAttribute("selected", "selected");
		}
		writeLine(" >");

		
		return EVAL_BODY_BUFFERED;
		
	}
	
	String inner;

	public int doAfterBody() throws JspException {
		  BodyContent bc = getBodyContent();
	      // get the bc as string
		  inner = bc.getString();
	      // clean up
	      bc.clearBody();
	  
	    return SKIP_BODY;
	}

	public int doEndTag() throws JspException  {
		writeLine(inner);
		writeLine("</option>");
		return EVAL_PAGE;
	}

	protected String getValue() {
		return value;
	}

	protected void setValue(String value) {
		this.value = value;
	}

	protected boolean isChecked() {
		return isChecked;
	}

	protected void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	@Override
	public void releaseState() {
		// TODO implement AbstractBodyTagSupport.releaseState
		
	}
}