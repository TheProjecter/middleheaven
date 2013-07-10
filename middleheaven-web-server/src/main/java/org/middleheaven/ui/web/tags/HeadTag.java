package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

public class HeadTag extends AbstractBodyTagSupport{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -398280890235321766L;

	public int doStartTag() throws JspException {
		writeLine("<head>");
		TagContext context = new TagContext(pageContext);
		String contextPath = context.getContextPath();
		String signature = "<script type=\"text/javascript\" encoding=\"ISO-8859-1\" src=\"" + contextPath + "/scripts/jquery.js\"></script>";

		writeLine(signature);
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
			writeLine(query);
		}
		writeLine("</head>");
		query =null;
		return EVAL_PAGE;
	}

	@Override
	public void releaseState() {
		// no-op
	}
}
