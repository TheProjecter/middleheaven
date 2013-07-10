package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * Selectes a value to be matched by a Case tag.
 */
public class SelectTag extends AbstractBodyTagSupport{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9073558569555531230L;
	private Object value ;

	Object getValue(){
		return value;
	}
	
	public void setValue(Object value){
		this.value = value;
	}

	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}
	
	String query =null;

	public int doAfterBody() throws JspException {
		  BodyContent bc = getBodyContent();
	      // get the bc as string
	      query = bc.getString();
	      // clean up
	      bc.clearBody();
	  
	    return SKIP_BODY;
	}

	public int doEndTag() throws JspException  {
		if (query!=null){
			write(query);
		}
		query =null;
		return EVAL_PAGE;
	}

	@Override
	public void releaseState() {
		// TODO implement AbstractBodyTagSupport.releaseState
		
	}
}
