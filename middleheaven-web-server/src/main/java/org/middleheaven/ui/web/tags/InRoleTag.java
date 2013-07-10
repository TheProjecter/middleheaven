package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.aas.AccessRequest;
import org.middleheaven.aas.Subject;
import org.middleheaven.process.ContextScope;


public class InRoleTag extends AbstractBodyTagSupport{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1406889297832883448L;
	String roleName;
	private String query =null;
	private String varName;
	
	public void setRole(String role){
		this.roleName = role;
	}
	
	public void setVar(String varName){
		this.varName = varName;
	}
	
	protected Subject getSubject(){

		AccessRequest ar =  new TagContext(this.pageContext).getAttribute(ContextScope.REQUEST, AccessRequest.class.getName(), AccessRequest.class);
		
		return ar ==null ? null : ar.getSubject();
	}
	
	protected boolean isInRole(String roleName){
		final Subject subject = getSubject();
		return subject!=null && subject.isInRole(roleName);
	}
	
	public final int doAfterBody() throws JspException {
		  BodyContent bc = getBodyContent();
	      // get the bc as string
	      query = bc.getString();
	      // clean up
	      bc.clearBody();
	  
	    return SKIP_BODY;
	}

	public final int doEndTag() throws JspException  {
		if (query!=null){
			write(query);
		}
		return EVAL_PAGE;
	}
	
	public int doStartTag() throws JspException {
		final boolean inRole = isInRole(roleName);
		pageContext.setAttribute(varName, Boolean.valueOf(inRole));
		
		if (inRole){
			return EVAL_BODY_BUFFERED;
		}else {
			return SKIP_BODY;
		}
	}

	@Override
	public void releaseState() {
		// no-op
	}

}
