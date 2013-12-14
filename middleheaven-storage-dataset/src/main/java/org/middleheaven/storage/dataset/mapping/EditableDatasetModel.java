/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

/**
 * 
 */
public interface EditableDatasetModel extends DatasetModel{

	public void setName(String name);


	public void setHardName(String hardName);


	/**
	 * @param cFunction
	 */
	public void addDatasetColumnModel (EditableDatasetColumnModel column);


	/**
	 * @param name
	 * @return
	 */
	public EditableDatasetColumnModel getColumn(String name);
	
}
