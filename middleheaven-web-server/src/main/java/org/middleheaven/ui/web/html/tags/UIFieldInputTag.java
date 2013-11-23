package org.middleheaven.ui.web.html.tags;

import java.io.Serializable;

import javax.servlet.jsp.JspException;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.web.tags.TagContext;

public class UIFieldInputTag extends AbstractUIComponentBodyTagSupport{

	private static final long serialVersionUID = 2674939376659644287L;
	
	private Serializable value;
	private String type;
	private boolean required = false;
	private boolean readOnly = false;
	private int maxLength;
	private int minLength;
	
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public Class<? extends UIComponent> getComponentType() {
		return UIField.class;
	}

	protected void populateProperties(TagContext attributeContext, UIComponent templateComponent) {
		this.setFamilly("field:" + type);
		
		UIField uic = (UIField)templateComponent;
		
		uic.getReadStateProperty().set(UIReadState.computeFrom(this.getVisible(), this.getEnabled(), readOnly));
		uic.getRequiredProperty().set(this.required);
		uic.getEnableProperty().set(this.getEnabled());
		uic.getValueProperty().set(value);
		uic.getMaxLengthProperty().set(this.maxLength);
		
	}
	
	public int doStartTag() throws JspException{
		return EVAL_BODY_BUFFERED;
	}
	
	
	@Override
	public void releaseState() {
		// no-op
	}


}
