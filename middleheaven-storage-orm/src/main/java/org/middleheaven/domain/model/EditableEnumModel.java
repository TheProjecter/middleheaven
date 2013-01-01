package org.middleheaven.domain.model;

public interface EditableEnumModel extends EnumModel {

	public void addValueMaping(Object value, Object persistable);
	
}
