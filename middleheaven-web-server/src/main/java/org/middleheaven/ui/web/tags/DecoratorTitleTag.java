package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.web.rendering.Page;

public class DecoratorTitleTag extends AbstractTagSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7295084614383636521L;
	private String defaultTitle;

	public void setDefault(String defaultTitle){
		this.defaultTitle = defaultTitle;
	}
	
	public int doStartTag() throws JspException {
		try {

			Page page = (Page)pageContext.getRequest().getAttribute(Page.PAGE);
			
			if (page.getTitle() != null){
				pageContext.getOut().print(page.getTitle());
			} else {
				pageContext.getOut().print(defaultTitle);
			}
			

			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
}
