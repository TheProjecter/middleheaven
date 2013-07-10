package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.validation.InvalidationReason;
import org.middleheaven.validation.InvalidationSeverity;
import org.middleheaven.validation.ValidationResult;


public class InvalidationReasonsListTag extends AbstractBodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5164654093651283835L;
	Iterator<InvalidationReason> iterator;
	private String varName = "item"; 
	private ValidationResult result;
	private InvalidationSeverity severity = null;

	public void setVar(String varName){
		this.varName = varName;
	}

	public String getVar(){
		return varName;
	}

	public void setResult(ValidationResult result){
		this.result = result;
	}

	public void setReasons(Iterable<InvalidationReason> result){
		this.iterator = result== null ? null :result.iterator();
	}

	public void setSeverity(String severity){
		this.severity = InvalidationSeverity.valueOf(severity);
	}

	public int doStartTag() throws JspException{
		if (iterator==null){
			if (result!=null){
				if (severity==null){
					this.iterator = result.iterator();
				} else {
					this.iterator = result.iterator(severity);
				}
				
				if (iterator.hasNext()){
					return EVAL_BODY_BUFFERED;
				}
			}
		}
		
		releaseState();
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

	public int doEndTag() throws JspException {
		releaseState();
		return EVAL_PAGE;
	}
	
	@Override
	public void releaseState() {
		this.iterator = null;
		this.result = null;
	}

}
