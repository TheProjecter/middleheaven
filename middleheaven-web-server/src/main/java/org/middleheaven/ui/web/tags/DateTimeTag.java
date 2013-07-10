package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.quantity.time.TimeUtils;

public class DateTimeTag extends AbstractTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7494225297324079280L;
	private Object value;


	public void setValue(Object value) {
		this.value = value;
	}

	public int doStartTag() throws JspException {
		try {

			pageContext.getOut().print(localize(TimeUtils.from(value), TimepointFormatter.Format.DATE_AND_TIME));

			return SKIP_BODY;
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
	}

	public int doEndTag() {
		return EVAL_PAGE;
	}

}
