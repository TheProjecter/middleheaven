package org.middleheaven.ui.web.tags;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.quantity.time.Period;

public class CacheControlTag extends AbstractTagSupport{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7050460872123036883L;
	private boolean cache = false;
	private int milisecoundsFromNow = (int)Period.days(30).milliseconds();

	public void setCache(boolean cache){
		this.cache = cache;
	}
	
	public void setExpiresIn(int secoundsFromNow){
		this.milisecoundsFromNow = secoundsFromNow * 1000;
	}
	
	public int doStartTag() throws JspException {
		try {
			final HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
			
			if (!cache){
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);

			} else {

				response.setDateHeader("Expires", milisecoundsFromNow);
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
