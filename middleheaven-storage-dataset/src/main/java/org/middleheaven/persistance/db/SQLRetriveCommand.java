package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.middleheaven.persistance.db.mapping.DataBaseMapper;

/**
 * 
 */
public class SQLRetriveCommand extends SQLConditionableCommand implements RetriveDataBaseCommand {


    private PreparedStatement ps;
	
	public SQLRetriveCommand(RDBMSDialect dialect,String sql, Collection<ValueHolder> values){
		super(dialect,sql,values);
	}

	@Override
	public ResultSet getResult() throws SQLException {
		return ps.executeQuery();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean execute(DataBaseMapper Function, Connection connection,
			QueryParameters parameters) throws SQLException {
		   
		ps = super.prepareStatement(Function,connection, parameters);
		   
		return true;
	}

	

}
