package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;

/**
 * Store cmomand. Inserts new data in the table.
 */
public class SQLStoreCollectionCommand implements DataBaseCommand {

	final private String sql;
	final private Collection<DataRow> data;
	final private RDBMSDialect dialect;

	
	public SQLStoreCollectionCommand(RDBMSDialect dialect , Collection<DataRow> data, String sql){
		this.dialect = dialect;
		this.data = data;
		this.sql = sql;
	}
	
	public RDBMSDialect getDialect(){
		return dialect;
	}
	
	@Override
	public boolean execute(DataBaseMapper Function, Connection connection, QueryParameters parameters) throws SQLException {

		if (data.size() > 1 && dialect.supportsBatch()){
		
			PreparedStatement ps = connection.prepareStatement(sql , ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
			
			PreparedStatementStorable pss = new PreparedStatementStorable(Function, this.dialect, ps);
			
			for (DataRow s : data){
				
				pss.copy(s);
				ps.addBatch();
				
			}
			return ps.executeBatch().length>0;
		} else if (!data.isEmpty()){
			PreparedStatement ps = connection.prepareStatement(sql,ResultSet.TYPE_FORWARD_ONLY , ResultSet.CONCUR_READ_ONLY);
			
			int count = 0;
			PreparedStatementStorable pss = new PreparedStatementStorable(Function,this.dialect,ps);
			for (DataRow s : data){
				
				pss.copy(s);
			
				count += ps.executeUpdate();
			}
			
			return count >0;
		} else {
			return false;
		}
		

	}

	@Override
	public String getSQL() {
		return sql;
	}


}
