package org.middleheaven.persistance.db.dialects;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

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
import org.middleheaven.persistance.db.metamodel.SequenceModel;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.CriterionOperator;

/**
 * Dialect for Postgress.
 */
public class PostgressDialect extends SequenceSupportedDBDialect{

	private static final String SEQUENCE_PREFIX = "pgseq_";
	
	public PostgressDialect() {
		super("\"", "\"", ".");
	}

	@Override
	public DataSetCriteriaInterpreter newCriteriaInterpreter(DataBaseMapper DataBaseMapper) {
		return new PostgressCriteriaInterpreter(this, DataBaseMapper);

	}
	
	@Override
	protected void appendInlineCreateTableColumnPrimaryKeyConstraint(Clause sql, DBColumnModel column){
		sql.append(" CONSTRAINT PK_").append(column.getName().getQualifier()).append("_").append(column.getName().getDesignation()).append(" PRIMARY KEY ");
	}

	protected String hardSequenceName(String logicName){

		return SEQUENCE_PREFIX.concat(logicName.toLowerCase()); //avoid name colision
	}
	
	protected String logicSequenceName(String hardName){
		return hardName.substring(SEQUENCE_PREFIX.length());
	}
	
	@Override
	protected <T> RetriveDataBaseCommand createNextSequenceValueCommand(String sequenceName) {
		return new SQLRetriveCommand(this,
				new StringBuilder("SELECT nextval('")
				.append(hardSequenceName(sequenceName)) 
				.append("') as sequenceValue")
				.toString() ,
				new LinkedList<ValueHolder>()
		);
	}
	
	@Override
	public EditionDataBaseCommand createCreateSequenceCommand(SequenceModel sequence) {
		
		StringBuilder sql = new StringBuilder("CREATE SEQUENCE ")
		.append(hardSequenceName(sequence.getName())) 
		.append(" INCREMENT BY ").append(sequence.getIncrementBy())
		.append(" MINVALUE ").append(sequence.getStartWith())
		.append(" START WITH " ).append(sequence.getStartWith());
		

		return new SQLEditCommand(this,sql.toString());
	}
	
	@Override
	public RDBMSException handleSQLException(SQLException e) {
		String msg = e.getMessage();
		if (e.getNextException()!=null){
			msg += "\n" + e.getNextException().getMessage();
		}
		return new RDBMSException(msg);
	}
	
	@Override
	public void writeQueryHardname(Clause buffer , QualifiedName hardname){

		buffer
		.append(startDelimiter())
		.append(hardname.getQualifier().toLowerCase())
		.append(endDelimiter())
		.append(fieldSeparator())
		.append(startDelimiter())
		.append(hardname.getDesignation().toLowerCase())
		.append(endDelimiter());
	}

	protected void writeEditionHardname(Clause buffer , String tableName, String columnName){
		buffer.append(this.startDelimiter());
		buffer.append(columnName.toLowerCase());
		buffer.append(this.endDelimiter());
		
	}
	
	@Override
	public void writeJoinTableHardname(Clause joinClause, String hardNameForEntity) {
		joinClause.append(startDelimiter())
		.append(hardNameForEntity.toLowerCase())
		.append(endDelimiter());
	}
	
	public void writeJoinField(Clause joinClause, String alias ,String fieldName) {
		joinClause
		.append(alias)
		.append(fieldSeparator())
		.append(startDelimiter())
		.append(fieldName)
		.append(endDelimiter());
	}
	
	@Override
	protected void writeEnclosureHardname(Clause buffer , String hardname){
		buffer.append(startDelimiter());
		buffer.append(hardname.toLowerCase());
		buffer.append(endDelimiter());
	}

	private static class PostgressCriteriaInterpreter extends AbstractRDBMSDataSetCriteriaInterpreter{

		public PostgressCriteriaInterpreter(RDBMSDialect dataBaseDialect, DataBaseMapper DataBaseMapper) {
			super(dataBaseDialect, DataBaseMapper);
		}

//		protected void writeFromClause(StringBuilder queryBuffer){
//
//			// FROM ClAUSE
//			queryBuffer.append(" FROM ");
//			queryBuffer.append(model().getEntityHardName().toLowerCase());
//
//		}
		
		@Override
		protected void writeEndLimitClause(SearchPlan plan, Clause selectBuffer){
			if ( plan.getMaxCount()>0){
				selectBuffer.append(" LIMIT ").append( plan.getMaxCount());
				if ( plan.getOffset()>1){
					selectBuffer.append(" OFFSET ").append( plan.getOffset() - 1);
				}
			}
		}
		

		protected void writeLikeClause(Clause criteriaBuffer,
					boolean caseSensitive,  ColumnValueLocator left, CriterionOperator op, ColumnValueLocator right, Collection<ValueHolder> paramsValues, DBColumnModel columnModel) {
				
			writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);
			
			if (op.isNegated()){
				criteriaBuffer.append(" NOT");
			}
			if (caseSensitive){
				criteriaBuffer.append(" LIKE "); // case sensitive
			} else {
				criteriaBuffer.append(" ILIKE "); // case insensitive
			}
			
			writeColumnValueLocator(criteriaBuffer, right, paramsValues, columnModel);
		}
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
		switch (column.getType()){ // TODO verify postgree documentatino for types
		case DATE:
			sql.append("date");
			break;
		case DATETIME:
			sql.append("timestamp");
			break;
		case TIME:
			sql.append("time");
			break;
		case TEXT:
			if (column.getSize() < 1){
				throw new IllegalArgumentException("Column " + column.getName()+ " size must be set");
			}
			sql.append("varchar (").append(column.getSize()).append(")");
			break;
		case CLOB:
		case MEMO:
			sql.append("text");
			break;
		case INTEGER:
			sql.append("bigint");
			break;
		case LOGIC:
			sql.append("boolean");
			break;
		case SMALL_INTEGER:
			sql.append("int");
			break;
		case DECIMAL:
			sql.append("numeric (").append(column.getPrecision()).append(",").append(column.getSize()).append(")");
			break;
		case BLOB:
		
		default:
			throw new PersistanceException(column.getType() + " is not convertible to a native column type");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveDataBaseCommand createExistsDatabaseCommand(String name) {
		
		Clause sql = new Clause("SELECT COUNT(*) = 1  FROM pg_catalog.pg_database WHERE datname = '" + name + "'");

		return new SQLRetriveCommand(this,sql.toString(), Collections.<ValueHolder>emptyList());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveDataBaseCommand createExistsSchemaCommand(String schemaName) {
		Clause sql = new Clause("SELECT COUNT(*) = 1  FROM pg_catalog.pg_namespace WHERE nspname = '" + schemaName + "'");

		return new SQLRetriveCommand(this,sql.toString(), Collections.<ValueHolder>emptyList());
	}


}
