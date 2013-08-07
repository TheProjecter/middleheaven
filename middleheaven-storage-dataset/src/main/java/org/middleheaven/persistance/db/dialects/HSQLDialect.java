package org.middleheaven.persistance.db.dialects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;

import javax.sql.DataSource;

import org.middleheaven.persistance.PersistanceException;
import org.middleheaven.persistance.db.AbstractRDBMSDataSetCriteriaInterpreter;
import org.middleheaven.persistance.db.Clause;
import org.middleheaven.persistance.db.DataSetCriteriaInterpreter;
import org.middleheaven.persistance.db.EditionDataBaseCommand;
import org.middleheaven.persistance.db.RDBMSDialect;
import org.middleheaven.persistance.db.RDBMSException;
import org.middleheaven.persistance.db.RetriveDataBaseCommand;
import org.middleheaven.persistance.db.SQLEditCommand;
import org.middleheaven.persistance.db.SQLRetriveCommand;
import org.middleheaven.persistance.db.SearchPlan;
import org.middleheaven.persistance.db.TableAlreadyExistsException;
import org.middleheaven.persistance.db.ValueHolder;
import org.middleheaven.persistance.db.datasource.EmbeddedDataSource;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DataBaseModel;
import org.middleheaven.persistance.db.metamodel.EditableColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.persistance.db.metamodel.EditableDataBaseModel;
import org.middleheaven.persistance.db.metamodel.SequenceModel;
import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.util.QualifiedName;

/**
 * HSQL dialect.
 */
public class HSQLDialect extends SequenceSupportedDBDialect{

	public HSQLDialect() {
		super("'", "'", ".");
	}

	public  boolean supportsBatch(){
		return false;
	}

	// HSQL storedProcedures
	public static boolean containsMatch(String target, String search) {
		search = search.replaceAll("%", "");
		return target.toLowerCase().contains(search.toLowerCase());
	}

	public static boolean endsWithMatch(String target, String search) {
		search = search.replaceAll("%", "");
		return target.toLowerCase().endsWith((search.toLowerCase()));
	}

	public static boolean startsWithMatch(String target, String search) {
		search = search.replaceAll("%", "");
		return target.toLowerCase().startsWith((search.toLowerCase()));
	}
	// end stored procedures

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public DataSetCriteriaInterpreter newCriteriaInterpreter(DataBaseMapper dataBaseMapper) {
		return new HSQLCriteriaInterpreter(this, dataBaseMapper);
	}

	@Override
	public RDBMSException handleSQLException(SQLException e) {
		if (e.getMessage().startsWith("Table already exists")){
			return new TableAlreadyExistsException();
		}
		return new RDBMSException(e);
	}

	@Override
	public void writeJoinTableHardname(Clause joinClause, String hardNameForEntity) {
		joinClause.append(hardNameForEntity);
	}


	@Override
	public void writeJoinField(Clause joinClause, String alias ,String fieldName) {
		joinClause.append(alias).append(fieldSeparator()).append(fieldName);
	}

	@Override
	public EditionDataBaseCommand createCreateSequenceCommand(SequenceModel sequence) {

		StringBuilder sql = new StringBuilder("CREATE SEQUENCE ")
		.append(hardSequenceName(sequence.getName())) 
		.append(" AS INTEGER ")
		.append(" START WITH " ).append(sequence.getStartWith())
		.append(" INCREMENT BY ").append(sequence.getIncrementBy());

		return new SQLEditCommand(this,sql.toString());
	}

	//	@Override
	//	public void writeEditionHardname(Clause buffer , QualifiedName hardname){
	//		if (!hardname.getName().isEmpty()){
	//			buffer.append(hardname.getName().toLowerCase());
	//		}
	//	}

	@Override
	public void writeEnclosureHardname(Clause buffer , String hardname){
		buffer.append(hardname);
	}

	@Override
	public void writeQueryHardname(Clause buffer , QualifiedName hardname){
		if (hardname.isAlias()){
			buffer.append(hardname.getDesignation().toLowerCase());
		} else {
			buffer.append(hardname.getQualifier().toLowerCase())
			.append(fieldSeparator())
			.append(hardname.getDesignation().toLowerCase());

		}
	}


	private static class HSQLCriteriaInterpreter extends AbstractRDBMSDataSetCriteriaInterpreter{

		public HSQLCriteriaInterpreter(RDBMSDialect dataBaseDialect, DataBaseMapper dataBaseMapper) {
			super(dataBaseDialect, dataBaseMapper);
		}

		//		@Override
		//		protected void writeFromClause(String alias , Clause queryBuffer){
		//
		//			// FROM ClAUSE
		//			queryBuffer.append(" FROM ");
		//			queryBuffer.append(model().getEntityHardName().toLowerCase());
		//
		//			if (alias!=null){
		//				queryBuffer.append(" AS ") 
		//				.append(alias);
		//			}
		//		}
		//		

		protected void writeStartLimitClause(SearchPlan plan , Clause selectBuffer){
			if (plan.isDistinct()){
				selectBuffer.append(" DISTINCT ");
			} 

			if (!plan.isCountOnly() && plan.hasMaxCount()){
				int offset = 0;
				if (plan.getOffset()>1){
					offset = plan.getOffset()-1;
				} 

				selectBuffer.append(" LIMIT ").append(offset).append(" ").append(plan.getMaxCount());

			}
		}
	}


	@Override
	protected <T> RetriveDataBaseCommand createNextSequenceValueCommand(String sequenceName) {
		return new SQLRetriveCommand( this,
				new StringBuilder("SELECT NEXT VALUE FOR ")
		.append(hardSequenceName(sequenceName))
		.append(" FROM INFORMATION_SCHEMA.SYSTEM_SEQUENCES")
		.toString(),
		new LinkedList<ValueHolder>()
				);
	}

	@Override
	public boolean supportsCountLimit() {
		return true;
	}

	@Override
	public boolean supportsOffSet() {
		return true;
	}

	@Override
	public EditionDataBaseCommand createCreateTableCommand(EditableDBTableModel tm){

		Clause sql = new Clause("CREATE CACHED TABLE ");
		sql.append(tm.getName());
		sql.append("(\n ");
		for (DBColumnModel cm : tm){
			sql.append(cm.getName().getDesignation());
			sql.append(" ");
			appendNativeTypeFor(sql , cm);
			if(!cm.isNullable()){
				sql.append(" NOT ");
			} 
			sql.append(" NULL ");
			// TODO handle multicolumn primary key
			if (cm.isKey()){
				sql.append("PRIMARY KEY");
			} 
			sql.append(",\n");
		}
		sql.removeLastCharacters(2);
		sql.append(")");
		return new SQLEditCommand(this,sql.toString());
	}

	@Override
	protected void appendNativeTypeFor(Clause sql, DBColumnModel column) {
		if (column == null){
			throw new IllegalArgumentException("Column is required");
		}

		if (column.getType() == null){
			throw new IllegalArgumentException("Column type is required");
		}
		switch (column.getType()){
		case DATE:
		case DATETIME:
		case TIME:
			sql.append("datetime");
			break;
		case TEXT:
			sql.append("varchar (").append(column.getSize()).append(")");
			break;
		case MEMO:
			sql.append("varchar");
			break;
		case INTEGER:
			sql.append("bigint");
			break;
		case SMALL_INTEGER:
		case LOGIC:
			sql.append("int");
			break;
		case DECIMAL:
			sql.append("numeric (").append(column.getPrecision()).append(",").append(column.getSize()).append(")");
			break;
		default:
			throw new PersistanceException(column.getType() + " is not convertible to a native column type");
		}

	}

	public DataBaseModel readDataBaseModel(String catalog, DataSource ds) {
		Connection con=null;
		PreparedStatement psTables = null;
		PreparedStatement psColumns = null;
		ResultSet tables = null;
		PreparedStatement psSequences = null;
		ResultSet sequences = null;

		try{
			con = ds.getConnection();

			EditableDataBaseModel dbm = new EditableDataBaseModel();

			psTables = con.prepareStatement(
					"SELECT table_name FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE TABLE_SCHEM = 'PUBLIC' AND TABLE_TYPE = 'TABLE'"
					);
			psColumns = con.prepareStatement(
					"SELECT column_name,data_type, column_size, nullable, decimal_digits FROM INFORMATION_SCHEMA.SYSTEM_COLUMNS WHERE TABLE_SCHEM = 'PUBLIC' AND TABLE_NAME = ? "
					);


			tables = psTables.executeQuery();
			while (tables.next()) {

				final String hardTableName = tables.getString(1);

				EditableDBTableModel tm = new EditableDBTableModel(hardTableName.toLowerCase());

				psColumns.setString(1, hardTableName);

				ResultSet columns = psColumns.executeQuery();
				while (columns.next()) {

					ColumnValueType type = this.typeFromNative(columns.getInt(2));
					EditableColumnModel col = new EditableColumnModel(columns.getString(1).toLowerCase(), type);
					if (type.isTextual()){
						col.setSize(columns.getInt(3));
					}
					if (type.isDecimal()){
						col.setPrecision(columns.getInt(5));
						col.setSize(columns.getInt(3));
					}

					col.setNullable(columns.getInt(4) == 1 );
					tm.addColumn(col);
				}

				columns.close();


				dbm.addDataBaseObjectModel(tm);

			}


			psSequences = con.prepareStatement(
					"SELECT  sequence_name , start_with , increment  FROM INFORMATION_SCHEMA.SYSTEM_SEQUENCES WHERE SEQUENCE_SCHEMA = 'PUBLIC'"
					);

			sequences = psSequences.executeQuery();
			while (sequences.next()) {
				SequenceModel sm = new SequenceModel(logicSequenceName(
						sequences.getString(1)) , 
						sequences.getInt(2), 
						sequences.getInt(3)
						);
				dbm.addDataBaseObjectModel(sm);
			}

			return dbm;
		} catch (SQLException e) {
			throw this.handleSQLException(e);
		} finally {			
			closeResultSet(tables);
			closePreparedStatement(psColumns);
			closePreparedStatement(psTables);
			closeResultSet(sequences);
			closePreparedStatement(psSequences);
			closeConnection(con);
		}
	}

	private void closePreparedStatement(PreparedStatement ps){
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException e) {
			throw this.handleSQLException(e);
		}
	}

	private void closeResultSet(ResultSet rs){
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			throw this.handleSQLException(e);
		}
	}

	private void closeConnection(Connection con){
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			throw this.handleSQLException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * HSQL only has one database. However it can have multiple schemas.
	 * This method will return a command to inspect if the given schema exists.
	 */
	@Override
	public RetriveDataBaseCommand createExistsDatabaseCommand(String name) {
		// HSQL only has one database.
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean existsDatabase(String name, DataSource datasource) {
		// method is overrided for special implementation
		if (datasource instanceof EmbeddedDataSource) {
			EmbeddedDataSource ds = (EmbeddedDataSource)datasource;
			return ds.getCatalog().equalsIgnoreCase(name);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveDataBaseCommand createExistsSchemaCommand(String schemaName) {
		// HSQL only has one database. However it can have multiple schemas.

		Clause sql = new Clause("select count(*) = 1  from INFORMATION_SCHEMA.SYSTEM_SCHEMAS WHERE TABLE_SCHEM = '" + schemaName.toUpperCase() + "'");

		return new SQLRetriveCommand(this,sql.toString(), Collections.<ValueHolder>emptyList());

	}




}
