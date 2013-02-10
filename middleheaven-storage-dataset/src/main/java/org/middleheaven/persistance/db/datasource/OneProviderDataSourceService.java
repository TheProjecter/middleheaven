/**
 * 
 */
package org.middleheaven.persistance.db.datasource;

import javax.sql.DataSource;

/**
 * 
 */
class OneProviderDataSourceService implements DataSourceService {

	

	private DataSourceProvider provider;
	
	/**
	 * Constructor.
	 * @param provider
	 */
	public OneProviderDataSourceService(DataSourceProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataSource getDataSource(String name) {
		return provider.getDataSource();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDataSourceProvider(String name, DataSourceProvider provider) {
		throw new UnsupportedOperationException("Can not use");
	}

}
