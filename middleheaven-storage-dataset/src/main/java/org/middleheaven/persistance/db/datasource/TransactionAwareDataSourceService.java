/**
 * 
 */
package org.middleheaven.persistance.db.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.middleheaven.transactions.TransactionService;

class TransactionAwareDataSourceService implements DataSourceServiceClosable{

	Map <String , DataSourceProvider> sources = new HashMap <String , DataSourceProvider>();
	private TransactionService transactionService;

	/**
	 * @param transactionService 
	 * 
	 */

	public TransactionAwareDataSourceService(TransactionService transactionService){
		this.transactionService = transactionService;
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
		sources.put(name, new AutoTransactionalDataSourceProviderAdapter(provider, transactionService));
	}

	/**
	 * 
	 */
	public void close(){

	}
}