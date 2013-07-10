package org.middleheaven.persistance.db.dialects;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.middleheaven.core.reflection.PropertyHandler;
import org.middleheaven.core.reflection.PropertyNotFoundException;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.persistance.PersistanceException;
import org.middleheaven.persistance.criteria.building.ColumnValueLocator;
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
import org.middleheaven.persistance.db.ValueHolder;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DataBaseModel;
import org.middleheaven.persistance.db.metamodel.DataBaseObjectAlreadyExistsException;
import org.middleheaven.persistance.db.metamodel.EditableColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.persistance.db.metamodel.EditableDataBaseModel;
import org.middleheaven.persistance.db.metamodel.SequenceModel;
import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.CriterionOperator;

public class Oracle10gDialect extends SequenceSupportedDBDialect{

	public Oracle10gDialect() {
		super("\"", "\"", ".");
	}

	@Override
	public DataSetCriteriaInterpreter newCriteriaInterpreter(DataBaseMapper mapper) {
		return new OracleCriteriaInterpreter(this, mapper);
	}


	public void writeEditionHardname(Clause buffer , QualifiedName hardname){

			buffer.append(startDelimiter());
			buffer.append(hardname.getDesignation().toLowerCase());
			buffer.append(endDelimiter());
		
	}
	
	public List<String> resolveCatalog(DataSource ds) {
		try {
			// the catalog is in fact the user schema.
			// the user must be returned
			ds = unwrapp(ds);
			
			// faster
			PropertyHandler pa = Introspector.of(ds.getClass()).inspect().properties().named("login").retrive();

			return Collections.singletonList(pa.getValue(ds).toString().toUpperCase());
		} catch (PropertyNotFoundException e){
			
			return Collections.emptyList();
		}

	}
	
	protected static DataSource unwrapp(DataSource ds){
		try{
			if (ds.isWrapperFor(DataSource.class)){
				return unwrapp(ds.unwrap(DataSource.class));
			} else {
				return ds;
			}
		} catch (SQLException e) {
			throw new PersistanceException(e);
		}
	}
	
	public RDBMSException handleSQLException(SQLException e) {
		String msg = e.getMessage();
		int code = e.getErrorCode();
		
		if (code==955){
			throw new DataBaseObjectAlreadyExistsException();
		}
		if (e.getNextException()!=null){
			msg += "\n" + e.getNextException().getMessage();
		}
		return new RDBMSException("[" + this.getClass().getSimpleName()+ "]" + msg);
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

			ResultSet tables = md.getTables( null ,catalog ,null,null);
			try{
				while (tables.next()) {
					if ("TABLE".equals(tables.getString(4))){
						EditableDBTableModel tm = new EditableDBTableModel(tables.getString(3));

						ResultSet columns = md.getColumns(null, catalog, tm.getName(), null);
						try{
							while (columns.next()) {

								ColumnValueType type = this.typeFromNative(columns.getInt(5));
								EditableColumnModel col = new EditableColumnModel(columns.getString(4), type);
								if (type.isTextual()){
									col.setSize(columns.getInt(7));
								}
								if (type.isDecimal()){
									col.setPrecision(columns.getInt(7));
									col.setSize(columns.getInt(9));
								}

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
			} finally{
				tables.close();
			}




			return dbm;
		} catch (SQLException e) {
			throw this.handleSQLException(e);
		} finally {
			if(con!=null){
				try {
					con.close();
				} catch (SQLException e) {
					throw this.handleSQLException(e);
				}
			}
		}
	}

	@Override
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
				sql.append("PRIMARY KEY ");
			} 
			sql.append(",\n");
		}
		sql.removeLastCharacters(2);
		sql.append(")");
		return new SQLEditCommand(this,sql.toString());
	}

	private  class OracleCriteriaInterpreter extends AbstractRDBMSDataSetCriteriaInterpreter{

		public OracleCriteriaInterpreter(RDBMSDialect dataBaseDialect, DataBaseMapper dataBaseMapper) {
			super(dataBaseDialect, dataBaseMapper);
		}

		@Override
		protected void writeAliasSeparator(Clause queryBuffer){
			queryBuffer.append(" ");
		}
		
		@Override
		protected void writeAlias(Clause queryBuffer , String alias){
			queryBuffer.append(startDelimiter())
			.append(alias)
			.append(endDelimiter());
		}	

		@Override
		protected void writeEndLimitClause(SearchPlan plan, Clause selectBuffer){
			if (plan.getMaxCount()>0 && plan.getOffSet()>1){
				
				if (selectBuffer.endsWith(')')){
					selectBuffer.append(" AND ");
				}
				selectBuffer.append("rownum between ")
				.append(plan.getOffSet())
				.append(" and ")
				.append(plan.getOffSet() + plan.getMaxCount()-1);
			}
		}


		protected void writeLikeClause(Clause criteriaBuffer,
					boolean caseSensitive,  ColumnValueLocator left, CriterionOperator op, ColumnValueLocator right, Collection<ValueHolder> paramsValues, DBColumnModel columnModel) {
				
				if (caseSensitive){
					
					writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);
					
					if (op.isNegated()){
						criteriaBuffer.append(" NOT");
					}
					
					criteriaBuffer.append(" LIKE ? "); // case sensitive
				} else {
					
					criteriaBuffer.append(" UPPER(");
					writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);
					criteriaBuffer.append(")");
					
					if (op.isNegated()){
						criteriaBuffer.append(" NOT");
					}
					criteriaBuffer.append(" LIKE ");
					
					criteriaBuffer.append(" UPPER(");
					writeColumnValueLocator(criteriaBuffer, right, paramsValues, columnModel);
					criteriaBuffer.append(")");	
				}
				
			}
		
	}

	@Override
	protected <T> RetriveDataBaseCommand createNextSequenceValueCommand(String sequenceName) {
		return new SQLRetriveCommand(this,
				new StringBuilder("SELECT ")
				.append(hardSequenceName(sequenceName))
				.append(".nextval FROM dual")
				.toString() ,
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
			sql.append("timestamp");
			break;
		case TEXT:
			sql.append("nvarchar2 (").append(column.getSize()).append(")");
			break;
		case SMALL_INTEGER:
		case INTEGER:
			sql.append("number");
			break;
		case LOGIC:
			sql.append("number (1)");
			break;
		case DECIMAL:
			sql.append("number (").append(column.getPrecision()).append(",").append(column.getSize()).append(")");
			break;
		default:
			throw new PersistanceException(column.getType() + " is not convertible to a native column type");
		}
	}

	@Override
	public EditionDataBaseCommand createCreateSequenceCommand(SequenceModel sequence) {

		StringBuilder sql = new StringBuilder("CREATE SEQUENCE ")
		.append(hardSequenceName(sequence.getName())) // avoid name colision
		.append(" INCREMENT BY ").append(sequence.getIncrementBy())
		.append(" MINVALUE ").append(sequence.getStartWith())
		.append(" START WITH " ).append(sequence.getStartWith());


		return new SQLEditCommand(this,sql.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveDataBaseCommand createExistsDatabaseCommand(String name) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveDataBaseCommand createExistsSchemaCommand(String schemaName) {
		throw new UnsupportedOperationException("Not implememented yet");
	}
}
