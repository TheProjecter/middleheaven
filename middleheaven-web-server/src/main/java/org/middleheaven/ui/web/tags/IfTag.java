package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * Conditional test. Output the tag body if, and only if, {@code test} evaluates to {@code true}.
 */
public class IfTag extends AbstractBodyTagSupport{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3502804294774364959L;
	private boolean test ;

	public void setTest(boolean test){
		this.test = test;
	}

	public int doStartTag() throws JspException {

		if (test){
			return EVAL_BODY_BUFFERED;
		}else {
			return SKIP_BODY;
		}

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
		releaseState();
		return EVAL_PAGE;
	}

	@Override
	public void releaseState() {
		// no-op
		query =null;
	}
}
