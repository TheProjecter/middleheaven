/**
 * 
 */
package org.middleheaven.ui.models.form;

import java.io.Serializable;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.validation.Validator;

/**
 * 
 */
public class UIFieldDescription  implements Serializable {


	private static final long serialVersionUID = -1387763794702256216L;
	
	private boolean isRequired;
	private boolean isReadOnly;
	private String name;
	private LocalizableText caption; 
	private Validator validator;
	private int tabOrder;
	private Class<?> valueType;
	private Class<?> uiType;
	private String family;
	private String group;
	
	public UIFieldDescription (){
		
	}
	
	/**
	 * Obtains {@link String}.
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}



	/**
	 * Atributes {@link String}.
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}



	/**
	 * Obtains {@link Class<?>}.
	 * @return the uiType
	 */
	public <T extends UIComponent> Class<T> getUIType() {
		return (Class<T>) uiType;
	}

	/**
	 * Atributes {@link Class<?>}.
	 * @param uiType the uiType to set
	 */
	public void setUIType(Class<? extends UIComponent> uiType) {
		this.uiType = uiType;
	}




	/**
	 * Obtains {@link String}.
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}



	/**
	 * Atributes {@link String}.
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}



	/**
	 * Obtains {@link Class<?>}.
	 * @return the valueType
	 */
	public Class<?> getValueType() {
		return valueType;
	}
	/**
	 * Atributes {@link Class<?>}.
	 * @param valueType the valueType to set
	 */
	public void setValueType(Class<?> valueType) {
		this.valueType = valueType;
	}
	/**
	 * Obtains {@link boolean}.
	 * @return the isRequired
	 */
	public boolean isRequired() {
		return isRequired;
	}
	/**
	 * Atributes {@link boolean}.
	 * @param isRequired the isRequired to set
	 */
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	/**
	 * Obtains {@link boolean}.
	 * @return the isReadOnly
	 */
	public boolean isReadOnly() {
		return isReadOnly;
	}
	/**
	 * Atributes {@link boolean}.
	 * @param isReadOnly the isReadOnly to set
	 */
	public void setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	/**
	 * Obtains {@link String}.
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Atributes {@link String}.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Obtains {@link LocalizableText}.
	 * @return the caption
	 */
	public LocalizableText getCaption() {
		return caption;
	}
	
	/**
	 * Atributes {@link LocalizableText}.
	 * @param caption the caption to set
	 */
	public void setCaption(LocalizableText caption) {
		this.caption = caption;
	}
	
	/**
	 * Obtains {@link Validator}.
	 * @return the validator
	 */
	public Validator getValidator() {
		return validator;
	}
	
	/**
	 * Atributes {@link Validator}.
	 * @param validator the validator to set
	 */
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	/**
	 * Obtains {@link int}.
	 * @return the tabOrder
	 */
	public int getTabOrder() {
		return tabOrder;
	}
	/**
	 * Atributes {@link int}.
	 * @param tabOrder the tabOrder to set
	 */
	public void setTabOrder(int tabOrder) {
		this.tabOrder = tabOrder;
	}


	
	
}
