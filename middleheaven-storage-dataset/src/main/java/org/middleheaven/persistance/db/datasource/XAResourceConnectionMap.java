/**
 * 
 */
package org.middleheaven.persistance.db.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.middleheaven.persistance.DataAccessException;

/**
 * 
 */
class XAResourceConnectionMap implements XAResource{

	private ThreadLocal<XConnection> connectionHolder = new ThreadLocal<XConnection> ();
	private DataSourceProvider provider;
	private int transactionTimeout;

	public static class XConnection {
		
		
		public XConnection(Xid xid) {
			super();
			this.xid = xid;
		}
		
		public Xid xid;
		public DelegatingConnection connection;
	}
	
	/**
	 * Constructor.
	 * @param connection
	 * @param con
	 */
	public XAResourceConnectionMap(DataSourceProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * @return
	 */
	public Connection getConnection() {
		final XConnection xConnection = connectionHolder.get();
		if (xConnection == null || xConnection.connection == null){
			// no transacional controler connection. autocomit
			try {
				final Connection connection = provider.getDataSource().getConnection();
				connection.setAutoCommit(true);
				return connection;
			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
		}
		return xConnection.connection;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public Connection getConnection(String username, String password) {
		throw new UnsupportedOperationException("Please use getConnection()");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int prepare(Xid xid) throws XAException {
		XConnection tl = connectionHolder.get();
		
		// verify prepare
		
		if (tl == null ){
			throw new IllegalStateException("Resource was not started for this transaction before");
		}

		return XA_OK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Xid xid, int flags) throws XAException {
		XConnection tl = connectionHolder.get();
		
		if (tl == null ){
			tl = new XConnection(xid);
			connectionHolder.set(tl);
			if (tl.connection == null){
			
				try {
					final Connection connection = provider.getDataSource().getConnection();
					connection.setAutoCommit(false);
					tl.connection = new DelegatingConnection(connection);
				} catch (SQLException e) {
					throw new XAException(e.getMessage());
				}
			}
		} 
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit(Xid xid, boolean onePhase) throws XAException {
		XConnection tl = connectionHolder.get();
		
		if (tl != null ){
			try {
				tl.connection.original.commit();
			} catch (SQLException e) {
				throw new XAException(e.getMessage());
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback(Xid xid) throws XAException {
		XConnection tl = connectionHolder.get();
		
		if (tl != null ){
			try {
				tl.connection.original.rollback();
			} catch (SQLException e) {
				throw new XAException(e.getMessage());
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void end(Xid xid, int flags) throws XAException {
		XConnection tl = connectionHolder.get();
		
		if (tl != null ){
			closeConnection(tl.connection.original);
			connectionHolder.set(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forget(Xid xid) throws XAException {
		XConnection tl = connectionHolder.get();
		
		if (tl != null ){
			closeConnection(tl.connection.original);
			connectionHolder.set(null);
		}
	}

	private void closeConnection(Connection connection) throws XAException {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new XAException(e.getMessage());
		}
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSameRM(XAResource other) throws XAException {
		return (other instanceof XAResourceConnectionMap) && this.equals(other);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Xid[] recover(int flag) throws XAException {
		XConnection tl = connectionHolder.get();
		if (tl == null){
			return new Xid[0];
		} else {
			return new Xid[]{tl.xid};
		}
	
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTransactionTimeout() throws XAException {
		return transactionTimeout;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setTransactionTimeout(int seconds) throws XAException {
		this.transactionTimeout = seconds;
		return true;
	}


	

}
