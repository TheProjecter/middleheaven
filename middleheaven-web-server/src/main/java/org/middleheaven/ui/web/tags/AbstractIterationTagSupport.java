package org.middleheaven.ui.web.tags;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.middleheaven.util.collections.CollectionUtils;

public class AbstractIterationTagSupport extends AbstractBodyTagSupport {

	private Iterator<?> iterator;
	private String varName = "item"; 
	private Object currentItem;
	
	protected Iterator<?> iterator(){
		return iterator;
	}
	
	public void setVar(String varName){
		this.varName = varName;
	}
	
	@SuppressWarnings("rawtypes")
	public void setItems(Object items){
		if (items != null){
			if(items instanceof Iterable){
				this.iterator = ((Iterable)items).iterator();
			} else if (items.getClass().isArray()){
				this.iterator =  CollectionUtils.arrayIterator((Object[])items);
			}
		}
		
	}
	
	public final int doStartTag() throws JspException{

		
		if (beforeStart() && iterator!=null && iterator.hasNext()){
			
			positionFirst();
			
			if( iterator.hasNext()){
				return EVAL_BODY_BUFFERED;
			}
		}
//		releaseState();
		return SKIP_BODY;
	}
	
	public int doEndTag () throws JspException {
		releaseState();
		return EVAL_PAGE;
	}
	
	protected boolean beforeStart() {
		// allways start
		return true;
	}

	protected void positionFirst() {
		//no-op
	}

	public void releaseState(){
		this.currentItem = null;
		this.iterator = null;
	}
	
	public final void doInitBody (){
		increment();
	}
	
	
	protected Object getCurrentItem(){
		return currentItem;
	}
	
	protected void increment(){
		currentItem = iterator.next();
		pageContext.setAttribute(varName, currentItem);
	}
	
	protected boolean hasNextElement(){
		return iterator.hasNext();
	}
	
	public final int doAfterBody() throws JspException{
		
			try {
				getBodyContent().writeOut(getPreviousOut());
				getBodyContent().clear();
				
				if (hasNextElement()){
					increment();
					return EVAL_BODY_BUFFERED;
				}
				
				return SKIP_BODY;
			} catch (IOException e) {
				throw new JspTagException(e.getMessage());
			}

	}
	
}
