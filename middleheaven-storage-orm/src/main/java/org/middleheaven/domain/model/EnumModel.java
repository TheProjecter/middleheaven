package org.middleheaven.domain.model;

public interface EnumModel {

	
	public Class<?> getEnumType();
	
	public Class<?> getPersistableType();
	
	public Object getEnumFromValue(Object persistableValue);
	
	public Object getPersistableValue(Object enumValue);
}
