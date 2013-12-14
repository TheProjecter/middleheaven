/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.domain.store.EntityInstance;
import org.middleheaven.domain.store.EntityManagerProxyHandler;
import org.middleheaven.domain.store.MetaBeanEntityInstance;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.persistance.HashDataRow;
import org.middleheaven.persistance.SimpleDataColumn;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.reflection.inspection.ClassIntrospector;
import org.middleheaven.storage.annotations.DatasetInheritance;

/**
 * 
 */
class EditableEntityModelDataSetMapping implements EntityModelDataSetMapping {

	
	private String dataSetName;
	private DataStoreSchemaName dataStoreSchemaName;
	private DatasetInheritance inherintance;
	private EntityInstanceTypeMapper type;
	
	private Map<String, EntityInstanceTypeMapper>  Functions = new HashMap<String, EntityInstanceTypeMapper>();
	
	
	
	public void addInstanceFunction(String name, EntityInstanceTypeMapper Function){
		this.Functions.put(name, Function);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityInstanceTypeMapper getInstanceTypeMapper(String name) {
		return type;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDataSetName() {
		return dataSetName;
	}

	

	public void setDataStoreSchemaName(DataStoreSchemaName dataStoreSchemaName) {
		this.dataStoreSchemaName = dataStoreSchemaName;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataStoreSchemaName getSchemaName() {
		return dataStoreSchemaName;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSingleDataSetInheritance() {
		return this.inherintance == DatasetInheritance.ONE_DATASET_FOR_ALL || this.inherintance == DatasetInheritance.NO_INHERITANCE;
	}



	/**
	 * @param resolveDataSetNameFromEntity
	 */
	public void setDataSetName(String datasetName) {
		this.dataSetName = datasetName;
	}



	/**
	 * @param inherintance
	 */
	public void setInherintance(DatasetInheritance inherintance) {
		this.inherintance = inherintance;
	}



	/**
	 * @param type
	 */
	public void setTypeMapping(EntityInstanceTypeMapper type) {
		this.type = type;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow currentRow) {


		Object instance = this.type.read(currentRow, null, new DataColumnModel[0]);
		
		ClassIntrospector<?> instrospector = ClassIntrospector.loadFrom(type.getMappedClassName());
		
		instance = instrospector.newProxyInstance(
					new EntityManagerProxyHandler((MetaBeanEntityInstance) instance), 
					EntityInstance.class
				);
		
		return instance;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DataRow> write(EntityInstance instance) {
		
		HashDataRow row = new HashDataRow();
		
		for (DataColumnModel m : this.type.getColumns()){
			row.addColumn(new SimpleDataColumn(m)); 
		}
		
		this.type.write(null, instance, row, new DataColumnModel[0]);
		
		return Collections.<DataRow>singleton(row);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityInstanceTypeMapper getTypeMapper() {
		return type;
	}





}
