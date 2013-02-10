/**
 * 
 */
package org.middleheaven.persistance.db.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.middleheaven.transactions.TransactionService;


class AutoTransactionalDataSource implements DataSource {

	/**
	 * 
	 */
	private XAResourceConnectionMap xaResourceMap;
	private DataSource original;

	public AutoTransactionalDataSource (DataSourceProvider provider , TransactionService transactionService){
		this.xaResourceMap = new XAResourceConnectionMap(provider);
		this.original = provider.getDataSource();
		transactionService.enlistResource(xaResourceMap);
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection() throws SQLException {
		
		return xaResourceMap.getConnection();
		
	}

	/**
	 * {@inheritDoc}
	 */
	public Connection getConnection(String username, String password)
			throws SQLException {
		return xaResourceMap.getConnection(username, password);
	}

	/**
	 * {@inheritDoc}
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return original.getLogWriter();
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLoginTimeout() throws SQLException {
		return original.getLoginTimeout();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return original.isWrapperFor(iface);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLogWriter(PrintWriter out) throws SQLException {
		original.setLogWriter(out);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLoginTimeout(int seconds) throws SQLException {
		original.setLoginTimeout(seconds);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return original.unwrap(iface);
	}

}