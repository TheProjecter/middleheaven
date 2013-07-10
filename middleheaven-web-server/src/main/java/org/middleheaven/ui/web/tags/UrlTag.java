package org.middleheaven.ui.web.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.util.UrlStringUtils;


/**
 * Adds context information to an URL
 */
public class UrlTag extends AbstractTagSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7225123114703999809L;
	private String href;


	public void setHref(String value) {
		this.href = value;
	}
	

	public int doStartTag() throws JspException {
		try {

			
			pageContext.getOut().print(UrlStringUtils.addContextPath(((HttpServletRequest)pageContext.getRequest()).getContextPath(), href));

			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}
	
}
