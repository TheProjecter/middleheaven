/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import org.middleheaven.collections.Enumerable;


/**
 * Holds the meta data for the all datasets
 */
public interface DatasetRepositoryModel {

	public Enumerable<DatasetModel> models();
	
	/**
	 * @param entityName
	 * @return
	 */
	public DatasetModel getDataSetModel(String entityName);
}
