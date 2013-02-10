package org.middleheaven.persistance.db.metamodel;

import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.util.Hash;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.StringUtils;

public class EditableColumnModel implements DBColumnModel {

	private String name;
	private String hardName;
	private ColumnValueType type;
	

	private boolean version = false;
	private boolean indexed = false;
	private boolean unique = false;
	private boolean nullable = true;
	private int size = 0;
	private int precision = 0;
	
	DBTableModel tableModel;

	
	private String uniqueGroup;
	private boolean key;

	public EditableColumnModel(String hardName, ColumnValueType type) {
		super();
		if (StringUtils.isEmptyOrBlank(hardName)){
			throw new IllegalArgumentException("Hardname cannot be null nor empty");
		}
		this.hardName = hardName;
		this.type = type;
	}

	/**
	 * Copy Constructor.
	 * @param cm base {@link DBColumnModel} to copy.
	 */
	public EditableColumnModel(DBColumnModel cm) {
		this(cm.getName().getDesignation(), cm.getType());
		this.uniqueGroup = cm.getUniqueGroupName();
		this.indexed = cm.isIndexed();
		this.key = cm.isKey();
		this.name = cm.getLogicName();
		this.nullable = cm.isNullable();
		this.size = cm.getSize();
		this.precision = cm.getPrecision();
		this.version = cm.isVersion();
	}

	public DBTableModel getTableModel() {
		return tableModel;
	}

	public void setSize(int size){
		this.size = size;
	}

	public boolean isIndexed() {
		return indexed;
	}

	public void setIndexed(boolean indexed) {
		this.indexed = indexed;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getLogicName() {
		return name;
	}

	public void setLogicName(String name) {
		this.name = name;
	}

	public ColumnValueType getType() {
		return type;
	}

	public void setType(ColumnValueType type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}
	
	@Override
	public DBColumnModel duplicate() {
		return new EditableColumnModel(this); 
	}
	
	public int hashCode(){
		return Hash.hash(this.name).hashCode();
	}

	public boolean equals(Object other){
		return other instanceof EditableColumnModel && ((EditableColumnModel)other).name.equalsIgnoreCase(name) && 
		this.tableModel.equals(((EditableColumnModel)other).tableModel);
	}
	
	public String toString(){
		return tableModel.getName() + "." + this.hardName;
	}


	@Override
	public void setTableModel(DBTableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * 
	 * @param version
	 */
	public void setVersion(boolean version) {
		this.version = version;
	}

	@Override
	public boolean isVersion() {
		return version;
	}

	public void setUniqueGroup(String group) {
		this.uniqueGroup = group;
	}


	@Override
	public String getUniqueGroupName() {
		return uniqueGroup;
	}

	@Override
	public QualifiedName getName() {
		return QualifiedName.qualify(this.tableModel.getName() , this.hardName);
	}

	public void setIndexGroup(String indexGroup) {
		// TODO Auto-generated method stub
		
	}

	


	
	
}
