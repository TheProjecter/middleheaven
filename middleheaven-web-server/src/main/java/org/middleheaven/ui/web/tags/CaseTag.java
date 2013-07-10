package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * Matches the given value to the on of the parent Select tag and
 * outputs this tag content if they are equal.
 * 
 */
public class CaseTag extends AbstractBodyTagSupport{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1554501637541143103L;
	private Object match ;
	
	public void setMatch(Object match){
		this.match = match;
	}

	public int doStartTag() throws JspException {
		
		SelectTag choose = this.findAncestorTag(SelectTag.class);
		if (choose != null){
			boolean test = choose.getValue() == null ? match == null : choose.getValue().equals(match);
			
			if (test){
				return EVAL_BODY_BUFFERED;
			}else {
				return SKIP_BODY;
			}
		} else {
			throw new RuntimeException("Case tag must be nested inside a Select tag");
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
		query =null;
		return EVAL_PAGE;
	}

	@Override
	public void releaseState() {
		// TODO implement AbstractBodyTagSupport.releaseState
		
	}
}
