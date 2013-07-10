package org.middleheaven.ui.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;

import org.middleheaven.process.web.HttpUserAgent;

/**
 * Renders body according to the user agent (the browser)
 */
public class AgentTag extends AbstractBodyTagSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 130517229862281153L;
	String agentName;

	public void setAgent(String agentName){
		this.agentName = agentName;
	}
	

	public int doStartTag() throws JspException {
		
		TagContext context = new TagContext(this.pageContext);
		
	   
		HttpUserAgent httpAgent = context.getAgent();
		
		if (httpAgent.getBrowserInfo().getName().equalsIgnoreCase(this.agentName)){
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
		query =null;
	}
}
