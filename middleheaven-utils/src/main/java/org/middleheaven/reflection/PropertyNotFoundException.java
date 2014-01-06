package org.middleheaven.reflection;

public class PropertyNotFoundException extends ReflectionException {

	private static final long serialVersionUID = 7896237107156570745L;
	
	private String propertyName;
	private String typeName;

	public PropertyNotFoundException(String propertyName, String  typeName) {
		super("Property " + propertyName + " not found in " + typeName);
		this.propertyName = propertyName;
		this.typeName= typeName;
	}

	/**
	 * Obtains {@link String}.
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Obtains {@link String}.
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}



}
