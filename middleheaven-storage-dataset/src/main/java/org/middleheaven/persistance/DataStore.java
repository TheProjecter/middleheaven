package org.middleheaven.persistance;


/**
 * Central interface for a set of data sets. 
 * A DataSetProvider interfaces with one or more data provider in order to retrieve persisted data.
 */
public interface DataStore {

	/**
	 * 
	 * @return the store name.
	 */
	public DataStoreName getName();
	
	
	public DataStoreSchema getDataStoreSchema(DataStoreSchemaName name);
	
	
}
