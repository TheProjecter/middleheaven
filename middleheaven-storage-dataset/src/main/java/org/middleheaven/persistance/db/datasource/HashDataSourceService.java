/**
 * 
 */
package org.middleheaven.persistance.db.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public class HashDataSourceService implements DataSourceServiceClosable{

	Map <String , DataSourceProvider> sources = new HashMap <String , DataSourceProvider>();

	/**
	 * 
	 */

	public HashDataSourceService(){
	}

	@Override
	public DataSource getDataSource(String name) {

		DataSourceProvider dsp = sources.get(name);
		if (dsp==null){
			throw new DataSourceProviderNotFoundException(name);
		}
		return dsp.getDataSource();
	}

	public void addDataSourceProvider(String name  , DataSourceProvider provider) {
		sources.put(name, provider);
	}

	/**
	 * 
	 */
	public void close(){

	}
}