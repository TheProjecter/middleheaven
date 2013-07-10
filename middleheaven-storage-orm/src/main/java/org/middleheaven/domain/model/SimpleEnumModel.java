package org.middleheaven.domain.model;

import org.middleheaven.collections.BoundMap;
import org.middleheaven.collections.HashBoundMap;

public class SimpleEnumModel implements EditableEnumModel {

	
	private Class enumType;
	private Class persistableType;
	private BoundMap<Object , Object> values = new HashBoundMap<Object, Object>();
			
	public SimpleEnumModel (Class enumType, Class persistableType){
		this.enumType = enumType;
		this.persistableType = persistableType;
	}
	
	@Override
	public Class<?> getEnumType() {
		return enumType;
	}

	@Override
	public Class<?> getPersistableType() {
		return persistableType;
	}

	public void addValueMaping(Object value, Object persistable){
		values.put(enumType.cast(value), persistable);
	}
	
	@Override
	public Object getEnumFromValue(Object persistableValue) {
		return enumType.cast(values.reversed().get(persistableValue));
	}

	@Override
	public Object getPersistableValue(Object enumValue) {
		return values.get(enumValue);
	}

}
