/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;

/**
 * 
 */
public class HashDatasetModel implements EditableDatasetModel {

	private String name;
	private String hardName;
	
	private final Map<String, EditableDatasetColumnModel > columns = new HashMap<String, EditableDatasetColumnModel>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHardName() {
		return hardName;
	}

	
	/**
	 * Atributes {@link String}.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Atributes {@link String}.
	 * @param hardName the hardName to set
	 */
	public void setHardName(String hardName) {
		this.hardName = hardName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<DatasetColumnModel> columns() {
		return Enumerables.asEnumerable(columns.values()).cast(DatasetColumnModel.class);
	}

	
	public void addDatasetColumnModel (EditableDatasetColumnModel column){
		this.columns.put(column.getName(),column);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EditableDatasetColumnModel getColumn(String name) {
		return columns.get(name);
	}


}
