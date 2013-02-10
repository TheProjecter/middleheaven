/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.DataStoreProvider;

/**
 * 
 */
public interface DataBaseService {


	public DataStoreProvider getDataStoreProvider();
	
	public DataStoreProvider getDataStoreProvider(DataSourceNameResolver dataSourceNameResolver);
}
