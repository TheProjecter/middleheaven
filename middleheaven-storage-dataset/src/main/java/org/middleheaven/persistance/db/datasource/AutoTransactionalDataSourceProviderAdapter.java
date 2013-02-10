/**
 * 
 */
package org.middleheaven.persistance.db.datasource;

import javax.sql.DataSource;

import org.middleheaven.transactions.TransactionService;

/**
 * 
 */
class AutoTransactionalDataSourceProviderAdapter implements DataSourceProvider {

	private DataSourceProvider provider;
	private TransactionService transactionService;
	private DataSource dataSource;

	/**
	 * Constructor.
	 * @param provider
	 * @param transactionService
	 */
	public AutoTransactionalDataSourceProviderAdapter(DataSourceProvider provider, TransactionService transactionService) {
		this.provider = provider;
		this.transactionService = transactionService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized DataSource getDataSource() {
		
		if (this.dataSource == null){
			this.dataSource = new AutoTransactionalDataSource(provider, transactionService);
		}
		
		return this.dataSource;
	}

}
