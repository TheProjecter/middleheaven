package org.middleheaven.persistance.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.DataRowStream;
import org.middleheaven.persistance.DataRowStreamException;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.model.DataColumnModel;

/**
 * Implementation of {@link DataRowStream} that encapsulates a {@link ResultSet}
 */
public class ResultSetDataRowStream implements DataRowStream {

	

	public static ResultSetDataRowStream newInstance (ResultSet rs, DataBaseMapper mapper, SearchPlan plan, RDBMSDialect dialect) throws SQLException{
		return new ResultSetDataRowStream(rs, mapper , ResultSetDataRow.newInstance(rs, dialect));
	}
	
	private final ResultSet rs;
	private final DataBaseMapper mapper;
	private final DataRow row;

	
	private ResultSetDataRowStream(ResultSet rs, DataBaseMapper mapper, ResultSetDataRow resultSetDataRow){
		this.rs = rs;
		this.mapper = mapper;
		this.row = resultSetDataRow;
	}
	
	@Override
	public boolean next() throws DataRowStreamException {
		try {
			return rs.next();
		} catch (SQLException e) {
			throw new DataRowStreamException(e);
		}
	}

	@Override
	public DataRow currentRow() throws DataRowStreamException {
		return row;
	}

	@Override
	public void close() throws DataRowStreamException {
		try {
			rs.close();
		} catch (SQLException e) {
			throw new DataRowStreamException(e);
		}
	}
	
	private class DataColumnIterator implements Iterator<DataColumn> {
		
		private Iterator<DataColumnModel> it;
		private DataRow row;

		public DataColumnIterator (Iterator<DataColumnModel> it , DataRow row){
			this.it = it;
			this.row = row;
		}
	
		@Override
		public boolean hasNext() {
			return it.hasNext();
		}

		@Override
		public DataColumn next() {
			return row.getColumn(it.next().getName());
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}