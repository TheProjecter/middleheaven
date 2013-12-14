package org.middleheaven.persistance.db.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DBTableModel;
import org.middleheaven.persistance.db.metamodel.EditableColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.StringUtils;

public abstract class AbstractDataBaseMapper implements DataBaseMapper {

	private final Map<String, DBTableModel> tableMappings = new  HashMap<String, DBTableModel >();
	private final Map<QualifiedName, DBColumnModel> logicToPhysicalColumnMappings = new  HashMap<QualifiedName, DBColumnModel>();
	private final Map<QualifiedName, QualifiedName> physicalToLogicColumnMappings = new  HashMap<QualifiedName, QualifiedName>();


	protected abstract ColumnValueType mapType(String type);

	protected ColumnTypeInfo getColumnTypeInfo(String type){
		ColumnTypeInfo c = new ColumnTypeInfo();
		
		c.setType(mapType(type));
		
		return c;
	}

	protected void addDataSetMapping(DataSetFunction dataSet) {

		EditableDBTableModel tbm = new EditableDBTableModel (dataSet.hardName);

		for (ColumnFunction cmap : dataSet.getColumns()){


			EditableColumnModel cm = new EditableColumnModel(
					new ColumnModelAdapter( cmap, tbm)
			);


			final QualifiedName logicName = QualifiedName.qualify(dataSet.name, cmap.name);
			logicToPhysicalColumnMappings.put( logicName, cm);
			physicalToLogicColumnMappings.put(cm.getName(),  logicName);
			
			tbm.addColumn(cm);

		}


		tableMappings.put(dataSet.name, tbm);

	}




	/**
	 * 
	 * {@inheritDoc}
	 */
	 @Override
	 public DBTableModel getTableForDataSet(String modelName) {

		 DBTableModel tb = tableMappings.get(modelName);

		 if (tb == null){
			 throw new IllegalModelStateException("Data set '" + modelName + "' is not mapped");
		 }

		 return tb;


	 }



	 @Override
	 public DBColumnModel getTableColumnModel(QualifiedName qn) {
		 //QualifiedName lw = QualifiedName.qualify(qn.getQualifier().toLowerCase(), qn.getName().toLowerCase());
		 return logicToPhysicalColumnMappings.get(qn);

	 }
	 
	 @Override
	 public QualifiedName getLogicQualifiedName(QualifiedName qn){
		 return this.physicalToLogicColumnMappings.get(qn);
	 }
		


	 protected static class DataSetFunction {

		 String name;
		 String hardName;

		 Map<String,ColumnFunction > columns = new HashMap<String,ColumnFunction>();

		 public String getName() {
			 return name;
		 }
		 public ColumnFunction getColumn(String name) {
			 return columns.get(name);
		 }
		 public void setName(String name) {
			 this.name = name; //.toLowerCase();
		 }
		 public String getHardName() {
			 return hardName;
		 }
		 public void setHardName(String hardName) {
			 this.hardName = hardName; //.toLowerCase();
		 }

		 public void addColumn(ColumnFunction cm) {
			 columns.put(cm.getName(), cm);
			 cm.setDataSetFunction(this);
		 }

		 public Collection<ColumnFunction> getColumns(){
			 return columns.values();
		 }


	 }

	 private class ColumnModelAdapter implements DBColumnModel {

		 private ColumnFunction columnFunction;
		 private DBTableModel tbm;

		 protected ColumnModelAdapter(ColumnFunction columnFunction, DBTableModel tbm) {
			 super();
			 this.columnFunction = columnFunction;
			 this.tbm = tbm;
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public String getLogicName() {
			 return columnFunction.getHardName();
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public DBColumnModel duplicate() {
			 return new EditableColumnModel(this); 
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public void setTableModel(DBTableModel beanTableModel) {
			 throw new UnsupportedOperationException();
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public boolean isNullable() {
			 return columnFunction.isNullable();
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public boolean isKey() {
			 return columnFunction.isKey();
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public boolean isVersion() {
			 return columnFunction.isVersion();
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public boolean isUnique() {
			 return !StringUtils.isEmptyOrBlank(this.columnFunction.getUniqueGroup());
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public String getUniqueGroupName() {
			 return this.columnFunction.getUniqueGroup();
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public int getSize() {
			 return columnFunction.getSize();
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public int getPrecision() {
			 return this.columnFunction.getSize();
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public ColumnValueType getType() {
			 return mapType (columnFunction.getType());
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public DBTableModel getTableModel() {
			 return this.tbm;
		 }

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public QualifiedName getName() {
			 return QualifiedName.qualify(tbm.getName(), this.columnFunction.getHardName());		}

		 /**
		  * {@inheritDoc}
		  */
		 @Override
		 public boolean isIndexed() {
			 return this.columnFunction.isIndexed();
		 }

	 }


	 protected static class ColumnFunction {

		 String name;
		 String hardName;
		 boolean indexed;
		 boolean nullable;
		 private boolean key;
		 private int size;
		 private int scale;
		 private boolean version;
		 private String uniqueGroupName;
		 private DataSetFunction dataSetFunction;
		 private String type;



		 public String getType() {
			 return type;
		 }
		 
		 public void setType(String type) {
			 this.type = type;
		 }
		 
		 public String getName() {
			 return name;
		 }
		 /**
		  * @param dataSetFunction
		  */
		 public void setDataSetFunction(DataSetFunction dataSetFunction) {
			 this.dataSetFunction = dataSetFunction;
		 }

		 /**
		  * @return
		  */
		 public String getUniqueGroup() {
			 return uniqueGroupName;
		 }



		 public void setUniqueGroup(String uniqueGroupName) {
			 this.uniqueGroupName = uniqueGroupName;
		 }
		 /**
		  * @return
		  */
		 public int getSize() {
			 return size;
		 }


		 public void setSize(int size) {
			 this.size = size;
		 }

		 public int getScale() {
			 return scale;
		 }

		 public void setScale(int scale) {
			 this.scale = scale;
		 }

		 /**
		  * @return
		  */
		 public boolean isVersion() {
			 return version;
		 }

		 public void setVersion(boolean version) {
			 this.version = version;
		 }


		 /**
		  * @return
		  */
		 public boolean isKey() {
			 return key;
		 }

		 public void setKey(boolean key) {
			 this.key = key;
		 }
		 /**
		  * @return
		  */
		 public boolean isNullable() {
			 return nullable;
		 }


		 public void setNullable(boolean nullable) {
			 this.nullable = nullable;
		 }

		 public void setName(String name) {
			 this.name = name;
		 }
		 public String getHardName() {
			 return hardName;
		 }
		 public void setHardName(String hardName) {
			 this.hardName = hardName;
		 }

		 public boolean isIndexed() {
			 return indexed;
		 }
		 public void setIndexed(boolean indexed) {
			 this.indexed =indexed;
		 }

	 }


	 /**
	  * {@inheritDoc}
	  */
	 @Override
	 public Collection<DBTableModel> getTableModels() {
		 return tableMappings.values();
	 }

	 protected static class ColumnTypeInfo {

		 ColumnValueType type;
		 Integer size;
		 Integer scale;

		 public ColumnValueType getType() {
			 return type;
		 }
		 public void setType(ColumnValueType type) {
			 this.type = type;
		 }
		 public Integer getSize() {
			 return size;
		 }
		 public void setSize(Integer size) {
			 this.size = size;
		 }
		 public Integer getScale() {
			 return scale;
		 }
		 public void setScale(Integer scale) {
			 this.scale = scale;
		 }


	 }



}
