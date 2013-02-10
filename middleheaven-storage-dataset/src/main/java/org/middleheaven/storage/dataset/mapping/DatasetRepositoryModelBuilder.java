/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.logging.Logger;
import org.middleheaven.persistance.model.ColumnValueType;

/**
 * 
 */
public final class DatasetRepositoryModelBuilder {

	
	public static DatasetRepositoryModelBuilder newInstance(){
		return new DatasetRepositoryModelBuilder();
	}
	
	
	private final List<DatasetRepositoryModelReader> readers = new ArrayList<DatasetRepositoryModelReader>(2);
	
	private DatasetRepositoryModelBuilder(){
		
	}
	
	public DatasetRepositoryModelBuilder addReader(DatasetRepositoryModelReader reader){
		readers.add(reader);
		return this;
	}
	
	public DatasetRepositoryModel build(){
		
		if (readers.isEmpty()){
			throw new IllegalStateException("No DatasetRepositoryModelReaders configured. Please add at least one DatasetRepositoryModelReader");
		}
		
		HashDatasetRepositoryModel model = new HashDatasetRepositoryModel();
		
		for (DatasetRepositoryModelReader reader : readers){
			reader.read(model);
		}
		
		
		// simple validation
		Logger log = Logger.onBookFor(this.getClass());
		
		for (DatasetModel m : model.models()){
			
			for (DatasetColumnModel c : m.columns()){
				
				EditableDatasetColumnModel ec = (EditableDatasetColumnModel) c;
				if (c.getValueType().equals(ColumnValueType.TEXT) && c.getSize() == 0 ){
					log.warn("Textual field {0}.{1} length was not set. Using 100 as default length" , m.getName() , c.getName());
					ec.setSize(100);
				}
				
			}
		}
		return model;
	}
	
}
