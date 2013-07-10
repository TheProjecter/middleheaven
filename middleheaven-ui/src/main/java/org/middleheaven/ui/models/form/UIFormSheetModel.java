/**
 * 
 */
package org.middleheaven.ui.models.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.validation.Validator;

/**
 * 
 */
public class UIFormSheetModel implements Serializable{

	private String name;
	private LocalizableText caption; 
	private Validator validator;
	private int tabOrder;
	private Class<?> valueType;
	private String group;
	
	private List<UIFieldDescription> fields = new ArrayList<UIFieldDescription>(5);

	public UIFormSheetModel (String name){
		this.name= name;
	}
	
	public void addField(UIFieldDescription field){
		this.fields.add(field);
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
	 * Obtains {@link List<UIFieldDescription>}.
	 * @return the fields
	 */
	public List<UIFieldDescription> getFields() {
		return fields;
	}
	

	
}
