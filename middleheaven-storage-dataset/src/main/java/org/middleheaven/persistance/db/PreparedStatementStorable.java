package org.middleheaven.persistance.db;

import java.io.Reader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.sql.rowset.serial.SerialClob;

import org.middleheaven.persistance.DataColumn;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DBTableModel;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.LongIdentity;

public class PreparedStatementStorable {

	private final PreparedStatement ps;
	private final DataBaseMapper mapper;
	private final RDBMSDialect dialect;
	
	public PreparedStatementStorable( DataBaseMapper mapper, RDBMSDialect dialect, PreparedStatement ps) {
		this.ps = ps; 
		this.mapper = mapper;
		this.dialect = dialect;

	}
	
	public void copy(DataRow row) throws SQLException {

		
		
		DBTableModel tbModel = mapper.getTableForDataSet(row.iterator().next().getModel().getDataSetModel().getName());
		
		int index = 1;
		for (DBColumnModel tbColumnModel : tbModel){
			
		    QualifiedName dsColumnName = mapper.getLogicQualifiedName(tbColumnModel.getName());
			
			
			if (dsColumnName == null){
				throw new IllegalStateException("No logic column model found for physical column " + tbColumnModel.getName()); 
			}
			
			DataColumn cm = row.getColumn(dsColumnName);
			
			if (cm == null){
				throw new IllegalStateException("No row column found for column " + dsColumnName); 
			}

			setColumn(index, cm.getValue() , tbColumnModel);
			index++;
		}
	}



	public void setColumn(int i, Object value, DBColumnModel cm ) throws SQLException {

		if (value == null){
			ps.setNull(i, dialect.typeToNative(cm.getType())); 
		} else if (value instanceof LongIdentity){
			ps.setLong(i,((LongIdentity)value).longValue());
		} else if (value instanceof IntegerIdentity){
			ps.setInt(i, ((IntegerIdentity)value).intValue());
		}  else if (cm.getType().isTemporal()){ 
			if (value instanceof Date ) {
				ps.setDate(i, new java.sql.Date(((Date)value).getTime()));
			} else if (value instanceof Calendar ) {
				ps.setTimestamp(i, new Timestamp(((Calendar)value).getTimeInMillis()));
			} else if (value instanceof CalendarDate){
				ps.setDate(i, new java.sql.Date(((CalendarDate)value).getMilliseconds()));
			} else if (value instanceof CalendarDateTime){
				ps.setTimestamp(i, new Timestamp(((CalendarDateTime)value).getMilliseconds()));
			}
		}  else {
			switch (cm.getType()){
			case CLOB:
				if (value instanceof Reader){ // TODO handler jdbc driver that has no Clob methods
					ps.setClob(i, (Reader) value);
				} else {
					ps.setClob(i, new StringReader(value.toString()));
				}
				break;
			case TEXT:
				ps.setString(i, value.toString());
				break;
			case SMALL_INTEGER:
			case INTEGER:
				if (value instanceof Long){
					ps.setLong(i,((Long)value).longValue());
				} else if (value instanceof Integer){
					ps.setInt(i, ((Integer)value).intValue());
				} else if (value instanceof Short){
					ps.setShort(i, ((Short)value).shortValue());
				} else if (value instanceof Character){
					ps.setInt(i, (int)((Character)value).charValue());
				}
				break;
			case LOGIC:
				ps.setBoolean(i, (Boolean)value);
				break;
			default:
				throw new UnsupportedOperationException(cm.getType() + " for valor of type " + value.getClass() + " cannot be set");
			}

		}

	}


}
