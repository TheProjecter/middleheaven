package org.middleheaven.ui.web.tags;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.process.ContextScope;

/**
 * A message print tag.
 * The message is localized before printing.
 * 
 */
public class MessageLocalizationTag extends AbstractTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5531094657811330299L;
	private List<Object> params = new LinkedList<Object>();
	private String key;
	private String scope;

	public void setScope(String scope) {
		this.scope = scope;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setParams(Object params){
		if (params instanceof String){
			if (params.toString().contains(",")){
				this.params.addAll(Arrays.asList(params.toString().split(",")));
			} else {
				this.params.add(params);
			}
		} else if (params instanceof Collection){
			this.params.addAll((Collection)params);
		} else if (params.getClass().isArray()){
			this.params.addAll(Arrays.asList((Object[])params));
		} else {
			this.params.add(params);
		}
	}

	public int doStartTag() {
		return EVAL_BODY_INCLUDE;

	}

	public int doEndTag() throws JspException {
		Serializable[] paramsObj = new Serializable[params.size()];

		paramsObj = params.toArray(paramsObj);			

		if(scope==null) {
			write(localize(LocalizableText.valueOf(key,paramsObj), null));
		} else {
			write(localize(LocalizableText.valueOf(key,paramsObj),ContextScope.valueOf(scope)));
		}
		
		this.params.clear();

		return EVAL_PAGE;
	}

	// used by MessageLocationParam
	void addParam(Object obj) {
		params.add(obj);
	}
	
	@Override
	public void release() {
		super.release();
		params.clear();
	}
}