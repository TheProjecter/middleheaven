package org.middleheaven.storage.criteria;

import org.middleheaven.data.DataType;

public class SingleObjectValueHolder implements FieldValueHolder {

	private static final long serialVersionUID = 2756122283381728051L;
	
	private Object value;
	private DataType dataType;

	public SingleObjectValueHolder(Object value,DataType dataType) {
		this.value = value;
		this.dataType = dataType;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public boolean isEmpty() {
		return value==null;
	}

	@Override
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public void setDataType(DataType type) {
		dataType = type;
	}

	@Override
	public boolean equalsValue(FieldValueHolder valueHolder) {
		return value.equals(valueHolder.getValue());
	}

}
