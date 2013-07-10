package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.web.rendering.Page;

public class DecoratorHeadTag extends AbstractTagSupport{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1265002588302795859L;

	public int doStartTag() throws JspException {
		try {

			Page page = (Page)pageContext.getRequest().getAttribute(Page.PAGE);
			
			pageContext.getOut().print(page.getHead());

			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
}
