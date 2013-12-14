/**
 * 
 */
package org.middleheaven.persistance.db.mapping;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DBTableModel;
import org.middleheaven.persistance.db.metamodel.EditableColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.storage.dataset.mapping.DatasetColumnModel;
import org.middleheaven.storage.dataset.mapping.DatasetModel;
import org.middleheaven.storage.dataset.mapping.DatasetRepositoryModel;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.function.Block;

/**
 * 
 */
public final class DatasetRepositoryModelDataBaseMapper implements DataBaseMapper {

	
	public static DatasetRepositoryModelDataBaseMapper newInstance(DatasetRepositoryModel model){
		
		final DatasetRepositoryModelDataBaseMapper Function =  new DatasetRepositoryModelDataBaseMapper();
		
		model.models().forEach(new Block<DatasetModel>(){

			@Override
			public void apply(final DatasetModel dsModel) {
				
				final EditableDBTableModel table = new EditableDBTableModel();
				
				table.setName(dsModel.getHardName());
				
				Function.tableMappings.put(dsModel.getName().toLowerCase(), table);
				
				
				dsModel.columns().forEach(new Block<DatasetColumnModel>(){

					@Override
					public void apply(DatasetColumnModel dsColumn) {
						
						EditableColumnModel column = new EditableColumnModel(dsColumn.getHardName(), dsColumn.getValueType());
						
						column.setLogicName(dsColumn.getName());
						column.setIndexed(dsColumn.isIndexed());
						column.setKey(dsColumn.isKey());
						column.setNullable(dsColumn.isNullable());
						column.setPrecision(dsColumn.getPrecision());
						
						if (column.getType().isTextual()){
							column.setSize(dsColumn.getSize());
						} else if (column.getType().isInteger() || column.getType().isDecimal() ){
							column.setSize(dsColumn.getScale());
						}
						
						column.setUnique(dsColumn.isUnique());
						column.setUniqueGroup(dsColumn.getUniqueGroupName());
						
						table.addColumn(column);
						
						final QualifiedName logicName = QualifiedName.qualify(dsModel.getName(), dsColumn.getName());
						Function.logicToPhysicalColumnMappings.put(logicName , column);
						Function.physicalToLogicColumnMappings.put(column.getName() , logicName);
					}
					
				});
				
			}
			
		});
		
		return Function;
		
	}

	private final Map<String, DBTableModel> tableMappings = new  HashMap<String, DBTableModel >();
	private final Map<QualifiedName, DBColumnModel> logicToPhysicalColumnMappings = new  HashMap<QualifiedName, DBColumnModel>();
	private final Map<QualifiedName, QualifiedName> physicalToLogicColumnMappings = new  HashMap<QualifiedName, QualifiedName>();


	private DatasetRepositoryModelDataBaseMapper (){
	
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBTableModel getTableForDataSet(String datasetName) {
		return tableMappings.get(datasetName.toLowerCase());
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBColumnModel getTableColumnModel(QualifiedName dataSetColumnName) {
		return logicToPhysicalColumnMappings.get(dataSetColumnName);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public QualifiedName getLogicQualifiedName(QualifiedName hardname) {
		return physicalToLogicColumnMappings.get(hardname);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DBTableModel> getTableModels() {
		return Collections.unmodifiableCollection(tableMappings.values());
	}




	


}
