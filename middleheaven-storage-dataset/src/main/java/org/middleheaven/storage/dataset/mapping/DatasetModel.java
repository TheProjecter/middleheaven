/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import org.middleheaven.collections.enumerable.Enumerable;

/**
 * 
 */
public interface DatasetModel {

	
	public String getName();
	
	public String getHardName();
	
	public Enumerable<DatasetColumnModel> columns(); 
	
	/**
	 * @param name
	 * @return
	 */
	public DatasetColumnModel getColumn(String name);
}
