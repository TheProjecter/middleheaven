/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.DataStoreName;
import org.middleheaven.persistance.DataStoreProvider;
import org.middleheaven.persistance.db.datasource.DataSourceService;

/**
 * 
 */
public class JDBCDataBaseService implements DataBaseService {

	
	private DataSourceService dsService;

	public JDBCDataBaseService (DataSourceService dsService){
		this.dsService = dsService;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public DataStoreProvider getDataStoreProvider() {
		
		return getDataStoreProvider(new DataSourceNameResolver(){

			@Override
			public String resolveDataSourceName(DataStoreName name) {
				return name.getName();
			}
			
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataStoreProvider getDataStoreProvider(DataSourceNameResolver dataSourceNameResolver) {

		return new RDBMSDataStoreProvider(dsService, dataSourceNameResolver);

	}

}
