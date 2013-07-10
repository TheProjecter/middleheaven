package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class JavaScriptEnabledTag extends AbstractBodyTagSupport{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8962379458619826527L;

	public int doStartTag() throws JspException {
		write("<script ");
		writeAttribute("text", "text/javascript");
		write(" ></script>");
		return EVAL_BODY_BUFFERED;
		
	}
	
	String body;

	public int doAfterBody() throws JspException {
		  BodyContent bc = getBodyContent();
	      // get the bc as string
	      body = bc.getString();
	      // clean up
	      bc.clearBody();
	  
	    return SKIP_BODY;
	}

	public int doEndTag() throws JspException  {
		writeLine("<noscript>");
		writeLine(body);
		writeLine("</noscript>");
		return EVAL_PAGE;
	}

	@Override
	public void releaseState() {
		// no-op
	}
}
