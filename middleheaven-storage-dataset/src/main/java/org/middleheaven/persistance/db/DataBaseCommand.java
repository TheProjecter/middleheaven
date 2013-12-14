package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.middleheaven.persistance.db.mapping.DataBaseMapper;


/**
 * Command to execute on a data base.
 */
public interface DataBaseCommand {

	/**
	 * 
	 * @return dialect object that created this command
	 */
	public RDBMSDialect getDialect();
	
	/**
	 * 
	 * @param connection
	 * @param parameters TODO
	 * @param storage
	 * @return
	 * @throws SQLException
	 */
	public boolean execute(DataBaseMapper Function, Connection connection, QueryParameters parameters)  throws SQLException;

	public String getSQL();

}
