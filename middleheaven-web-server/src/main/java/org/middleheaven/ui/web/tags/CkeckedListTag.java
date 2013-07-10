package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;


public class CkeckedListTag extends AbstractBodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2208199268202609425L;
	Iterator<CheckableListItem> iterator;
	private String varName = "item"; 
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVar(String varName){
		this.varName = varName;
	}
	
	public String getVar(){
		return varName;
	}
	
	public void setItems(Iterable<CheckableListItem> iterable){
		this.iterator = iterable.iterator();
	}
	
	public int doStartTag() throws JspException{
		writeLine("<div class=\"scrollableList\" >");
		write("<div class=\"scrollablelistItem\" ");
		writeAttribute("id", getId());
		writeLine(" >");
		
		if (iterator.hasNext()){
			return EVAL_BODY_BUFFERED;
		}
		return SKIP_BODY;
	}
	
	public void doInitBody (){
		Object obj = iterator.next();
		pageContext.setAttribute(varName, obj);
	}
	
	public int doAfterBody() throws JspException{
		
			try {
				getBodyContent().writeOut(getPreviousOut());
				getBodyContent().clear();
				
				if (iterator.hasNext()){
					Object obj = iterator.next();
					pageContext.setAttribute(varName, obj);
					return EVAL_BODY_BUFFERED;
				}
				
				return SKIP_BODY;
			} catch (IOException e) {
				throw new JspTagException(e.getMessage());
			}

	}
	
	public int doEndTag() throws JspTagException {
		writeLine("</div>");
		writeLine("</div>");
		
		return EVAL_PAGE;
	}
	
	public void release(){
		this.iterator = null;
	}

	@Override
	public void releaseState() {
		// TODO implement AbstractBodyTagSupport.releaseState
		
	}
}
