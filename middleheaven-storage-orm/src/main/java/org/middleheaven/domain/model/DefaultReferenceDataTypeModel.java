package org.middleheaven.domain.model;

import org.middleheaven.core.metaclass.MetaClass;


public class DefaultReferenceDataTypeModel implements ReferenceDataTypeModel {

	private String fieldName;
	private DataType dataType;
	private MetaClass targetType;
	private MetaClass targetFieldType;
	private MetaClass aggregationType;
	private DataType targetFieldDataType;
	
	protected DefaultReferenceDataTypeModel() {
		super();
	}
	
	public DefaultReferenceDataTypeModel(DataType dataType) {
		super();
		this.dataType = dataType;
	}

	@Override
	public DataType getDataType() {
		return dataType;
	}
	
	
	public void setTargetFieldName(String fieldName){
		this.fieldName = fieldName;
	}

	
	public void setTargetType(MetaClass targetType) {
		this.targetType =targetType; 
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String getTargetFieldName() {
		return fieldName;
	}
	
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public MetaClass getTargetType() {
		return targetType;
	}

	public void setAggregationType(MetaClass aggregationType) {
		this.aggregationType = aggregationType;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public MetaClass getAggregationType() {
		return aggregationType;
	}

	@Override
	public MetaClass getTargetFieldType() {
		return targetFieldType;
	}
	
	public void setTargetFieldType( MetaClass type) {
		this.targetFieldType = type;
	}

	/**
	 * @param dataType2
	 */
	public void setTargetFieldDataType(DataType dataType) {
		this.targetFieldDataType = dataType;
	}

	/**
	 * Obtains {@link DataType}.
	 * @return the targetFieldDataType
	 */
	public DataType getTargetFieldDataType() {
		return targetFieldDataType;
	}
	
	
}
