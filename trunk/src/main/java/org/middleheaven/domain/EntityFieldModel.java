package org.middleheaven.domain;

import org.middleheaven.storage.QualifiedName;

public interface EntityFieldModel {

	public QualifiedName getLogicName();
	public DataType getDataType();
	public boolean isTransient();
	public boolean isVersion();
	public boolean isIdentity();
	public Class<?> getValueClass();
	public boolean isUnique();
	
	public String getParam(String key);
	
}