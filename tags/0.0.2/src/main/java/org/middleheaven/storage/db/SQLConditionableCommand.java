package org.middleheaven.storage.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.middleheaven.storage.StorableFieldModel;

public abstract class SQLConditionableCommand implements ConditionableDataBaseCommand {

	private final String sql;
	private final Collection<ColumnValueHolder> data;
	private DataBaseDialect dialect;

	protected SQLConditionableCommand(DataBaseDialect dialect,String sql,Collection<ColumnValueHolder> data){
		this.dialect = dialect;
		this.data = data;
		this.sql = sql;
	}

	@Override
	public String getSQL() {
		return sql;
	}
	
	public DataBaseDialect getDialect(){
		return dialect;
	}
	
	protected final PreparedStatement prepareStatement(DataBaseStorage storage, Connection connection) throws SQLException{
		PreparedStatement ps = connection.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);

		PreparedStatementStorable pss = new PreparedStatementStorable(storage,ps);

		int param = 1;
		for (ColumnValueHolder vh : data){
			StorableFieldModel fm = vh.getStorableFieldModel();
			pss.setField(param, vh.getValue(), fm);
			param++;
		}

		return ps;
	}
	
	public String toString(){
		return sql;
	}

}