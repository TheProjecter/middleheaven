/**
 * 
 */
package org.middleheaven.persistance.model;

import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class DataColumnModelBean implements DataColumnModel {

	private DataSetModel dataSetModel;
	private QualifiedName name;
	private int size;
	private int precision;
	private boolean isNullable;
	private String indexGroup;
	private String uniqueGroup;
	private String primaryKeyGroup;
	private ColumnValueType type;
	private boolean version;
	
	public DataColumnModelBean(){}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSetModel getDataSetModel() {
		return this.dataSetModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualifiedName getName() {
		return this.name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return this.size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPrecision() {
		return this.precision;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNullable() {
		return this.isNullable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIndexGroup() {
		return this.indexGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUniqueGroup() {
		return this.uniqueGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPrimaryKeyGroup() {
		return this.primaryKeyGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInPrimaryKeyGroup() {
		return this.primaryKeyGroup != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInIndexGroup() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInUniqueGroup() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ColumnValueType getType() {
		return this.type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVersion() {
		return this.version;
	}

	/**
	 * Atributes {@link DataSetModel}.
	 * @param dataSetModel the dataSetModel to set
	 */
	public void setDataSetModel(DataSetModel dataSetModel) {
		this.dataSetModel = dataSetModel;
	}

	/**
	 * Atributes {@link QualifiedName}.
	 * @param name the name to set
	 */
	public void setName(QualifiedName name) {
		this.name = name;
	}

	/**
	 * Atributes {@link int}.
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Atributes {@link int}.
	 * @param precision the precision to set
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * Atributes {@link boolean}.
	 * @param isNullable the isNullable to set
	 */
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	/**
	 * Atributes {@link String}.
	 * @param indexGroup the indexGroup to set
	 */
	public void setIndexGroup(String indexGroup) {
		this.indexGroup = indexGroup;
	}

	/**
	 * Atributes {@link String}.
	 * @param uniqueGroup the uniqueGroup to set
	 */
	public void setUniqueGroup(String uniqueGroup) {
		this.uniqueGroup = uniqueGroup;
	}

	/**
	 * Atributes {@link String}.
	 * @param primaryKeyGroup the primaryKeyGroup to set
	 */
	public void setPrimaryKeyGroup(String primaryKeyGroup) {
		this.primaryKeyGroup = primaryKeyGroup;
	}

	/**
	 * Atributes {@link ColumnValueType}.
	 * @param type the type to set
	 */
	public void setType(ColumnValueType type) {
		this.type = type;
	}

	/**
	 * Marks this column as the version identifier
	 * @param version the version to set
	 */
	public void setVersion(boolean version) {
		this.version = version;
	}
	
	public String toString(){
		return this.name.toString();
	}

}
