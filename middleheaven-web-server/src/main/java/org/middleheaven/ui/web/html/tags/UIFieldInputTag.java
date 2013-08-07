package org.middleheaven.ui.web.html.tags;

import java.io.Serializable;

import javax.servlet.jsp.JspException;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIField;

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

	public UIComponent getUIComponent() {
		
		this.setFamilly("field:" + type);
		
		UIField uic = (UIField)super.getUIComponent();
		
		uic.getReadStateProperty().set(UIReadState.computeFrom(this.getVisible(), this.getEnabled(), readOnly));
		uic.getRequiredProperty().set(this.required);
		uic.getEnableProperty().set(this.getEnabled());
		uic.getValueProperty().set(value);
		uic.getMaxLengthProperty().set(this.maxLength);
		
		
		return uic;
	}
	
	public int doStartTag() throws JspException{
		return EVAL_BODY_BUFFERED;
	}
	
	
	@Override
	public void releaseState() {
		// no-op
	}


}
