package org.middleheaven.persistance.db.datasource;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.logging.Logger;
import org.middleheaven.persistance.PersistanceException;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.transactions.TransactionService;
import org.middleheaven.transactions.XAResourceAdapter;
import org.middleheaven.util.Maybe;


/**
 * Utilizes a HSQL database that is started by this in server mode
 * or in-process  mode.
 */
public class EmbeddedDataSource extends AbstractDataSource {

	private String catalog;
	private String login;
	private String pass;
	private String url;
	private URL location;
	private boolean inMemory;

	public EmbeddedDataSource(URL location, String catalog, String login, String pass) {
		this(false,location,catalog,login,pass);
	}

	public EmbeddedDataSource(String catalog, String login, String pass) {
		this(true,null,catalog,login,pass);
	}

	private EmbeddedDataSource(boolean inMemory, URL location, String catalog, String login, String pass) {

		this.inMemory = inMemory;
		this.location = location;
		this.catalog = catalog;
		if (inMemory){
			this.url = "jdbc:hsqldb:mem:" + catalog;
		} else {
			this.url = "jdbc:hsqldb:hsql://localhost/" + catalog;
		}
		this.login = login;
		this.pass = pass;

		try {
			Class.forName("org.hsqldb.jdbcDriver" ).newInstance();
		} catch (ClassNotFoundException e) {
			throw new DriverNotFoundException("Driver org.hsqldb.jdbcDriver was not found.");
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}


	}

	public String getCatalog(){
		return catalog;
	}
	
	public void start(){


	
		if(!inMemory){
			Logger.onBookFor(this.getClass()).info("Starting : {0}", url);
			
			String[] params = new String[]{"-database.0", location.toString(), "-dbname.0"};
	
			Introspector.of(Object.class).load("org.hsqldb.Server").invokeMain(params);
		} else {
			Logger.onBookFor(this.getClass()).info("Starting in memory database");
		}


	}

	public void stop(){

		Logger.onBookFor(this.getClass()).info("Stopping : {0}" , url);

		
		Connection con = null;
		PreparedStatement ps = null;
		try{
			try {
				con = riseConnection(this.login, this.pass);

				con.setAutoCommit(true);
				ps = con.prepareStatement("SHUTDOWN");
				
				ps.execute();

			} finally {
				if (ps != null){
					ps.close();
				}
				if (con!=null && !con.isClosed()){
					con.close();
				}
			}
		} catch (SQLException e){
			throw new PersistanceException(e);
		}
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getConnection(this.login,this.pass);
	}

	
	public Connection riseConnection(String nlogin, String npass) throws SQLException{
		return DriverManager.getConnection(this.url, nlogin,npass);
	}
	
	ThreadLocal<Connection> local = new ThreadLocal<Connection>();
	
	@Override
	public Connection getConnection(String nlogin, String npass) throws SQLException {
		
		Connection c = local.get();
		
		if ( c == null ){
			Maybe<TransactionService> maybeTransactionService = ServiceRegistry.getPossibleUnAvailableService(TransactionService.class);
			Connection con = riseConnection(nlogin,npass);
			
			if (maybeTransactionService.isPresent() && maybeTransactionService.get().isTransactional()){
				con.setAutoCommit(false);
		
				maybeTransactionService.get().enlistResource(new XAConnectionControl(con));
				
				c = new EmbededConnection(con);
				local.set(c);
			} else if (maybeTransactionService.isAbsent()){
				con.setAutoCommit(true);
				return con;
			} else {
				con.setAutoCommit(this.isAutoCommit());
				return con;
			}
		}
		
		return c;
	}
	
	public static class EmbededConnection extends DelegatingConnection  {

		public EmbededConnection(Connection original) {
			super(original);
		}

		
		public void commit () throws SQLException {
			// do nothing
		}
		
		public void rollback () throws SQLException {
			// do nothing
		}
		
		public void close() throws SQLException{
			// do nothing
		}
	}
	
	public  class XAConnectionControl extends XAResourceAdapter  {

		
		private Connection connection;

		public XAConnectionControl (Connection c){
			this.connection = c;
		}
		
		@Override
		public void commit(Xid xid, boolean onePhase) throws XAException {
			try {
				connection.commit();
			} catch (SQLException e) {
				throw new XAException(e.getMessage());
			}
		}

		@Override
		public void rollback(Xid xid) throws XAException {
			try {
				connection.rollback();
			} catch (SQLException e) {
				throw new XAException(e.getMessage());
			}
		}

		public void end(Xid xid, int flags){
			try {
				this.connection.close();
				local.set(null);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
		}
		
	}

}
