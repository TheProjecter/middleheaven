package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.middleheaven.logging.Log;
import org.middleheaven.storage.StorageException;
import org.middleheaven.storage.db.dialects.HSQLDialect;
import org.middleheaven.storage.db.dialects.MSDriverSQLServerDialect;
import org.middleheaven.storage.db.dialects.PostgressDialect;
import org.middleheaven.storage.db.dialects.SQLServerDialect;

/**
 * Select a data base dialect based on is own metadata
 * provided by <code>java.sql.DatabaseMetaData</code>
 * 
 *
 */
public final class DatabaseDialectFactory {

	private DatabaseDialectFactory(){}

	/**
	 * Determine the dialect that applies to the given {@link DataSource}
	 * @param dataSource the DataSource to connect to
	 * @return the correct {@link DataBaseDialect}
	 */
	public static DataBaseDialect getDialect(DataSource dataSource){

		// finds dialect dynamically
		Connection con =null;
		try {
			con =  dataSource.getConnection();
			
			DatabaseMetaData dbm = con.getMetaData();
			return getDialectForDataBase(dbm);

		} catch (SQLException e) {
			throw new StorageException(e);
		} finally {
			if (con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					throw new StorageException(e);
				}
			}
		}


	}
	private static DataBaseDialect getDialectForDataBase (DatabaseMetaData dbm) throws SQLException{
		String product = dbm.getDatabaseProductName();
		String version = dbm.getDatabaseProductVersion();
		String driverName = dbm.getDriverName();
		String driverVersion = dbm.getDriverVersion();

		Log.onBookFor(DatabaseDialectFactory.class).info(
				"Inicializing dialect for: {0} {1} usign driver {2} {3}" ,  
				product, 
				version,
				driverName, 
				driverVersion
		);

		if (product.equalsIgnoreCase("Microsoft SQL Server")){
			if (driverName.toLowerCase().startsWith("sqlserver")){
				return new MSDriverSQLServerDialect(); //version 08.00.0760 Microsoft Driver 2.2.0022
			} else {
				return new SQLServerDialect(); // version 08.00.0760 jTDS
			}
		} else if (product.equalsIgnoreCase("PostgreSQL")){
			return new PostgressDialect(); // 8.1.3 with corresponding driver, 9.0 with corresponding driver
		} else if (product.toUpperCase().startsWith("HSQL")){
			return new HSQLDialect(); // 1.8.0 with corresponding driver
		} else {
			throw new StorageException("Dialect not found for product " + product + "  " + version);
		}

	}

}