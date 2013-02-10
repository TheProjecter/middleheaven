package org.middleheaven.persistance.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.Iterator;

import javax.sql.DataSource;

import org.middleheaven.logging.Logger;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.PersistanceException;
import org.middleheaven.persistance.criteria.DataSetCriteria;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DBTableModel;
import org.middleheaven.persistance.db.metamodel.DataBaseModel;
import org.middleheaven.persistance.db.metamodel.EditableColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.persistance.db.metamodel.EditableDataBaseModel;
import org.middleheaven.persistance.db.metamodel.SequenceModel;
import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.collections.Enumerable;

public abstract class RDBMSDialect {

	private final String startDelimiter;
	private final String endDelimiter;
	private final String fieldSeparator;
	private final AliasResolver aliasResolver = new DefaultAliasResolver(); 


	/**
	 * 
	 * @param startDelimiter delimiter for the start of the table or column name
	 * @param endDelimiter delimiter for the start of the table or column name
	 * @param fieldSeparator separator between the table and column names.
	 */
	protected RDBMSDialect(String startDelimiter, String endDelimiter,String fieldSeparator) {

		this.startDelimiter = startDelimiter;
		this.endDelimiter = endDelimiter;
		this.fieldSeparator = fieldSeparator;
	}

	public String aliasFor(String name, boolean increment){
		return aliasResolver.aliasFor(name, increment);
	}

	public  QualifiedName aliasFor(QualifiedName name, String aliasPrefix){
		return aliasResolver.aliasFor(name, aliasPrefix);
	}


	/**
	 * 
	 * @return true if this dialect SQL syntax permits define a explicit limit to the number of rows returned in a query
	 */
	protected abstract boolean supportsCountLimit();

	/**
	 * 
	 * @return true if this dialect SQL syntax permits define a explicit offset for the rows returned
	 */
	protected abstract boolean supportsOffSet();

	/**
	 * 
	 * @return <code>true</code> if this dialect supports Batch storage, <code>false</code> otherwise.
	 */
	public  boolean supportsBatch(){
		return true;
	}

	protected String startDelimiter() {
		return startDelimiter;
	}

	protected String endDelimiter() {
		return endDelimiter;
	}

	protected String fieldSeparator() {
		return fieldSeparator;
	}

	public void writeQueryHardname(Clause buffer , QualifiedName hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname.getQualifier().toLowerCase());
		buffer.append(endDelimiter);
		buffer.append(fieldSeparator);
		buffer.append(startDelimiter);
		buffer.append(hardname.getDesignation().toLowerCase());
		buffer.append(endDelimiter);
	}

	protected void writeEditionHardname(Clause buffer ,DBColumnModel model){
		writeEditionHardname(buffer, model.getTableModel().getName(), model.getName().getDesignation());
	}

	protected void writeEditionHardname(Clause buffer , String tableName, String columnName){
		buffer.append(startDelimiter);
		buffer.append(tableName.toLowerCase());
		buffer.append(endDelimiter);
		if (!columnName.isEmpty()){
			buffer.append(fieldSeparator);
			buffer.append(startDelimiter);
			buffer.append(columnName.toLowerCase());
			buffer.append(endDelimiter);
		}
	}

	protected void writeEnclosureHardname(Clause buffer , String hardname){
		buffer.append(startDelimiter);
		buffer.append(hardname);
		buffer.append(endDelimiter);
	}

	/**
	 * Provides exception handling for {@link SQLException}s
	 * @param e the SQLException.
	 * @return the resulting RDBMSException
	 * @pattern Exception Handler Method
	 */
	public RDBMSException handleSQLException(SQLException e) {
		return new RDBMSException(e.getMessage());
	}

	/**
	 * Creates a {@link RetriveDataBaseCommand} based on the {@link SearchPlan} and the {@link DataBaseMapper}.
	 * @param plan the plan to execute.
	 * @param mapper the database-dataset mapper.
	 * @return the executable retrieve command
	 */
	public final RetriveDataBaseCommand createSelectCommand (SearchPlan plan, DataBaseMapper mapper){

		return newCriteriaInterpreter(mapper).translateRetrive(plan);
	}

	/**
	 * Creates a {@link DataBaseCommand} based on the {@link SearchPlan} and the {@link DataBaseMapper}.
	 * @param plan the plan to execute.
	 * @param mapper the database-dataset mapper.
	 * @return the executable command
	 */
	public DataBaseCommand createDeleteCommand(SearchPlan plan, DataBaseMapper mapper){

		return newCriteriaInterpreter(mapper).translateDelete(plan);

	}


	/**
	 * Creates insert command.
	 * @param data collection of storables to be inserted.
	 * @param model entity store model.
	 * @return the insert command.
	 */
	public  DataBaseCommand createInsertCommand(Collection<DataRow> data, DBTableModel dsModel) {
		Clause names = new Clause();
		Clause values = new Clause();

		for (DBColumnModel m : dsModel) {
			this.writeEditionHardname(names, m);
			names.append(",");
			values.append("?,");

		}

		names.removeLastChar();
		values.removeLastChar();

		Clause sql = new Clause("INSERT INTO ");
		this.writeEnclosureHardname(sql, dsModel.getName());

		sql.append(" (")
		.append(names)
		.append(") VALUES (")
		.append(values)
		.append(")");



		return new SQLStoreCollectionCommand(this,data,sql.toString());
	}

	public DataBaseCommand createUpdateCommand(Collection<DataRow> data, DBTableModel dsModel){
		Clause sql = new Clause("UPDATE ");

		this.writeEnclosureHardname(sql, dsModel.getName());

		sql.append(" SET ");

		// iterate all columns
		for (DBColumnModel cm : dsModel) {
			this.writeEditionHardname(sql, cm);
			sql.append("=? ,");
		}

		sql.removeLastChar().append(" WHERE ");

		// iterate all key columns
		for (DBColumnModel m  : dsModel.identityColumns()){
			this.writeEditionHardname(sql, m);
			sql.append("=?");
		}

		return new SQLStoreCollectionCommand(this, data, sql.toString());
	}

	/**
	 * Creates a {@link EditionDataBaseCommand} based on the {@link SequenceModel} to create a sequence construct in the RDBMS
	 * @param model the sequence model to create.
	 * @return the executable command
	 */
	public EditionDataBaseCommand createCreateSequenceCommand(SequenceModel model) {

		throw new UnsupportedOperationException(this.getClass().getName() + " does not support native sequence objects");
	}

	/**
	 * Only creates, does not remove
	 * @param tm the data base table model
	 * @return
	 */
	public EditionDataBaseCommand createAlterTableCommand(EditableDBTableModel tm) {

		Clause sql = new Clause("ALTER TABLE ");
		writeEnclosureHardname(sql, tm.getName());
		sql.append("\n");
		for (DBColumnModel cm : tm) {
			sql.append("ADD ");
			writeEnclosureHardname(sql,cm.getName().getDesignation());
			sql.append(" ");
			appendNativeTypeFor(sql , cm);
			sql.append(",\n");
		}

		sql.removeLastCharacters(2);

		return new SQLEditCommand(this,sql.toString());
	}

	/**
	 * @param dataBaseName
	 * @return
	 */
	protected abstract RetriveDataBaseCommand createExistsDatabaseCommand(String dataBaseName);

	public abstract RetriveDataBaseCommand createExistsSchemaCommand(String schemaName);


	public DataSetCriteriaInterpreter newCriteriaInterpreter(DataBaseMapper mapper) {
		return new AbstractRDBMSDataSetCriteriaInterpreter(this, mapper);
	}

	public abstract Sequence<Long> getSequence(DataSource ds, String identifiableName);


	protected boolean supportsIntervalOf(ColumnValueType dataType) {
		return !dataType.isTextual() && !ColumnValueType.BLOB.equals(dataType);
	}

	/**
	 * The dialect may add other specific table models to the current model
	 * @param dbModel
	 */
	public void extendsDatabaseModel(EditableDataBaseModel dbModel) {
		//no-op
	}

	public DataBaseModel readDataBaseModel(String catalog, DataSource ds) {
		Connection con=null;
		try{
			con = ds.getConnection();
			DatabaseMetaData md = con.getMetaData();
			if (md==null){
				throw new PersistanceException("Metadata is not supported");
			}
			EditableDataBaseModel dbm = new EditableDataBaseModel();


			ResultSet tables = md.getTables(catalog , null,null,null);
			while (tables.next()) {
				if ("TABLE".equals(tables.getString(4))){
					EditableDBTableModel tm = new EditableDBTableModel(tables.getString(3));

					ResultSet columns = md.getColumns(catalog, null, tm.getName(), null);
					try{
						while (columns.next()) {

							ColumnValueType type = this.typeFromNative(columns.getInt(5));
							EditableColumnModel col = new EditableColumnModel(columns.getString(4), type);
							if (type.isTextual()){
								col.setSize(columns.getInt(7));
							}
							if (type.isDecimal()){
								col.setPrecision((columns.getInt(7)));
								col.setSize(columns.getInt(9));
							}

							col.setLogicName(columns.getString(4));
							col.setNullable("YES".equals(columns.getString(18)));
							tm.addColumn(col);
						}

					} finally {
						columns.close();
					}
					dbm.addDataBaseObjectModel(tm);
				} else if ("SEQUENCE".equals(tables.getString(4))){
					dbm.addDataBaseObjectModel(new SequenceModel(logicSequenceName(tables.getString(3)),1,1));
				}
			}

			tables.close();

			return dbm;
		} catch (SQLException e) {
			throw this.handleSQLException(e);
		} finally {
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					throw this.handleSQLException(e);
				}
			}
		}
	}

	protected String hardSequenceName(String logicName){
		return "seq_".concat(logicName.toLowerCase());
	}

	protected String logicSequenceName(String hardName){
		return hardName.substring(4);
	}

	public ColumnValueType typeFromNative(int sqlType) {
		switch (sqlType){
		case Types.INTEGER:
			return ColumnValueType.INTEGER;
		case Types.TINYINT:	
			return ColumnValueType.SMALL_INTEGER;
		case Types.BIT:
		case Types.BOOLEAN:
			return ColumnValueType.LOGIC;
		case Types.DATE:
			return ColumnValueType.DATE;
		case Types.TIME:
			return ColumnValueType.TIME;
		case Types.TIMESTAMP:
			return ColumnValueType.DATETIME;
		case Types.VARCHAR:
		case Types.CHAR:
			return ColumnValueType.TEXT;
		case Types.CLOB:
			return ColumnValueType.MEMO;
		case Types.BLOB:
			return ColumnValueType.BLOB;
		case Types.DECIMAL:
			return ColumnValueType.DECIMAL;
		}
		return ColumnValueType.TEXT;
	}
	
	public int typeToNative(ColumnValueType dataType) {
		

			switch (dataType){
			case INTEGER:
				return Types.INTEGER;
			case DATETIME:
				return Types.TIMESTAMP;
			case TIME:
				return Types.TIME;
			case DATE:
				return Types.DATE;
			case LOGIC:
				return Types.BOOLEAN;
			case TEXT:
				return Types.VARCHAR;
			case BLOB:
				return Types.BLOB;
			case DECIMAL:
				return Types.DECIMAL;
			case MEMO:
			case CLOB:
				return Types.CLOB;
			case SMALL_INTEGER:
				return Types.TINYINT;
			default:
				throw new IllegalArgumentException("Cannot convert column type " + dataType.name() + " to SQL type" );
			}

	}

	public  EditionDataBaseCommand createCreateTableCommand(EditableDBTableModel tm){

		Clause sql = new Clause("CREATE TABLE ");
		writeEnclosureHardname(sql, tm.getName());


		sql.append("(\n ");
		for (DBColumnModel cm : tm){
			writeEnclosureHardname(sql,cm.getName().getDesignation());
			sql.append(" ");
			appendNativeTypeFor(sql , cm);
			if(!cm.isNullable()){
				sql.append(" NOT ");
			} 
			sql.append(" NULL ");
			if (cm.isKey()){
				appendInlineCreateTableColumnPrimaryKeyConstraint(sql,cm);
			} 
			sql.append(",\n");
		}
		sql.removeLastCharacters(2);
		sql.append(")");




		return new SQLEditCommand(this,sql.toString());
	}


	protected  void appendInlineCreateTableColumnPrimaryKeyConstraint(Clause sql, DBColumnModel column){
		sql.append(" CONSTRAINT PK_").append(column.getTableModel().getName().toUpperCase()).append("_").append(column.getName().getDesignation().toUpperCase());
	}

	protected abstract void appendNativeTypeFor(Clause sql, DBColumnModel columnModel);


	/**
	 * 
	 * @param indexComponents a collections of {@link DBColumnModel} that form the index.
	 * @param indexIsUnique <code>true</code> if the index is unique. Use <code>false</code> to create a non-unique index for speeding proposes.
	 * @return
	 */
	public EditionDataBaseCommand createCreateIndexCommand(Enumerable<DBColumnModel> indexComponents, boolean indexIsUnique){

		if (indexComponents.isEmpty()){
			throw new IllegalModelStateException("Cannot create an index without components");
		}

		final String tableName = indexComponents.getFirst().getTableModel().getName();

		StringBuilder sql = new StringBuilder("CREATE ");
		StringBuilder def = new StringBuilder(" ");
		StringBuilder name = new StringBuilder("idx_").append(tableName).append("_");

		if (indexIsUnique){
			sql.append(" UNIQUE ");
		}

		int maxCharsForName = Math.max(4, 26 / indexComponents.size()); 

		for (Iterator<DBColumnModel> it = indexComponents.iterator(); it.hasNext();){

			DBColumnModel cm = it.next();

			def.append(cm.getTableModel().getName()).append("_").append(cm.getName().getDesignation());

			name.append(StringUtils.subString(cm.getName().getDesignation(), maxCharsForName));

			if (it.hasNext()){
				def.append(", ");
				name.append("_");
			}
		}


		sql.append("INDEX ").append(name)
		.append(def)
		.append(" ON ").append(tableName);

		return new SQLEditCommand(this,sql.toString());
	}

	public void writeJoinTableHardname(Clause joinClause, String hardNameForEntity) {
		joinClause.append(startDelimiter())
		.append(hardNameForEntity)
		.append(endDelimiter());
	}


	public void writeJoinField(Clause joinClause, String alias ,String fieldName) {
		joinClause.append(startDelimiter())
		.append(alias)
		.append(endDelimiter())
		.append(fieldSeparator())
		.append(startDelimiter())
		.append(fieldName)
		.append(endDelimiter());
	}

	public DataBaseCommand createDeleteCommand(DataSetCriteria criteria, DBTableModel model) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param name
	 * @param datasource
	 * @return
	 */
	public boolean existsDatabase(String name, DataSource datasource) {
		ResultSet rs = null;
		Connection con = null;
		try {

			con = datasource.getConnection();

			RetriveDataBaseCommand command = this.createExistsDatabaseCommand(name);
			Logger.onBook("SQL").trace(command.toString());

			command.execute( null, con, null);
			rs = command.getResult();

			if (rs.next()){
				return rs.getBoolean(1);
			} else {
				return false;
			}
		} catch (SQLException e) {
			close(con);
			throw handleSQLException(e);
		}
	}

	/**
	 * @param con
	 */
	private void close(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			throw handleSQLException(e);
		}
	}




}
