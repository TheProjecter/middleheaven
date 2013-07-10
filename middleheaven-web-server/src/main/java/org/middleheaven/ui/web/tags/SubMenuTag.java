package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.ui.components.MenuItem;


public class SubMenuTag extends AbstractTagSupport {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4835136751065655858L;


	public int doStartTag() throws JspException{
		
		MenuTag top =  this.findAncestorTag(MenuTag.class);
		
		MenuItem current = top.getCurrentMenuItem();
		
		if (current.getChildrenCount()>0){
			writeLine(MenuTag.SUB_MENU_TAG);
		}
		
		return SKIP_BODY;
	}
	

	public int doEndTag() throws JspTagException{
		
		return EVAL_PAGE;
	}
	
}
