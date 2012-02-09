package org.middleheaven.persistance.model;

import org.middleheaven.util.QualifiedName;


/**
 * 
 */
public interface DataColumnsModel extends Iterable<DataColumnModel> {


	public DataColumnModel getDataColumnModel(String columnName);
	
	public boolean isEmpty();
	
	public int size();

	public boolean containsColumn(QualifiedName name);
}
