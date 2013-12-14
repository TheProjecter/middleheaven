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
public abstract class SQLConditionableCommand implements ConditionableDataBaseCommand {

	private final String sql;
	private final Collection<ValueHolder> values;
	private RDBMSDialect dialect;

	protected SQLConditionableCommand(RDBMSDialect dialect,String sql,Collection<ValueHolder> values ){
		this.dialect = dialect;
		this.values = values;
		this.sql = sql;
	}

	@Override
	public String getSQL() {
		return sql;
	}
	
	public RDBMSDialect getDialect(){
		return dialect;
	}
	
	protected final PreparedStatement prepareStatement(DataBaseMapper Function, Connection connection, QueryParameters parameters) throws SQLException{
		PreparedStatement ps = connection.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);

		PreparedStatementStorable pss = new PreparedStatementStorable(Function, this.dialect,ps);

		int param = 1;
		for (ValueHolder c : values){
			pss.setColumn(param, c.getValue(parameters), c.getModel());
			param++;
		}
		
		return ps;
	}
	
	public String toString(){
		return sql;
	}

}
