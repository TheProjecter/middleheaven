package org.middleheaven.persistance.db;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.persistance.criteria.DataSetConstraint;
import org.middleheaven.persistance.criteria.LogicConstraint;
import org.middleheaven.persistance.criteria.ResultColumnDefinition;
import org.middleheaven.persistance.criteria.ResultColumnDefinition.ResultFunction;
import org.middleheaven.persistance.criteria.building.ColumnGroupConstraint;
import org.middleheaven.persistance.criteria.building.ColumnNameValueLocator;
import org.middleheaven.persistance.criteria.building.ColumnValueConstraint;
import org.middleheaven.persistance.criteria.building.ColumnValueLocator;
import org.middleheaven.persistance.criteria.building.ExplicitValueLocator;
import org.middleheaven.persistance.criteria.building.IntervalValueLocator;
import org.middleheaven.persistance.criteria.building.MultipleValueLocator;
import org.middleheaven.persistance.criteria.building.OrderConstraint;
import org.middleheaven.persistance.criteria.building.ParameterValueLocator;
import org.middleheaven.persistance.db.mapping.DataBaseMapper;
import org.middleheaven.persistance.db.metamodel.DBColumnModel;
import org.middleheaven.persistance.db.metamodel.DBTableModel;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.CriterionOperator;
import org.middleheaven.util.criteria.JunctionCriterion;

/**
 * Base implementation for SQL based {@link DataSetCriteriaInterpreter}.
 */
public class AbstractRDBMSDataSetCriteriaInterpreter implements DataSetCriteriaInterpreter {


	private RDBMSDialect dataBaseDialect;
	private DataBaseMapper dataBaseMapper;

	/**
	 * 
	 * Constructor.
	 * @param dataBaseDialect the dialect used for interpretation
	 * @param criteria the criteria to interpret.
	 * @param reader 
	 */
	public AbstractRDBMSDataSetCriteriaInterpreter(RDBMSDialect dataBaseDialect, DataBaseMapper dataBaseMapper) {
		this.dataBaseDialect = dataBaseDialect;
		this.dataBaseMapper = dataBaseMapper;
	}

	/**
	 * 
	 * @return the current {@link RDBMSDialect}.
	 */
	protected RDBMSDialect dialect(){
		return this.dataBaseDialect;
	}

	/**
	 * 
	 * @return the current {@link DataBaseMapper}
	 */
	protected DataBaseMapper DataBaseMapper(){
		return dataBaseMapper;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public RetriveDataBaseCommand translateRetrive(SearchPlan plan){

		Clause clause = new Clause("SELECT ");
		Collection<ValueHolder> params = new LinkedList<ValueHolder>();

		// TODO resolve alias.

		// LIMITS: distinct and top n 
		writeStartLimitClause(plan, clause);

		// RETURN CLAUSE
		writeResultProjection(plan,clause);

		// FROM / JOIN CLAUSE 
		writeFromJoinClause(plan,clause);

		// WHERE CLAUSE
		writeWhereClause(plan,clause, params);

		// GROUP BY
		writeGroupByClause(plan,clause);

		// ORDER BY
		writeOrderByClause(plan,clause);

		// LIMITS: Limit and offset
		writeEndLimitClause(plan, clause);

		return new SQLRetriveCommand(dialect(),clause.toString(), params);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataBaseCommand translateDelete(SearchPlan plan){


		TableRelation dataSet = plan.getSourceDataSets().iterator().next();

		DBTableModel table = dataSet.getSourceTableModel();

		final Clause sqlBuilder = new Clause("DELETE FROM ");

		dialect().writeEnclosureHardname(sqlBuilder, table.getName());

		Collection<ValueHolder> row = new LinkedList<ValueHolder>();

		// WHERE CLAUSE
		writeWhereClause(plan, sqlBuilder, row);

		return new SQLDeleteCommand(dialect(),sqlBuilder.toString(),row);
	}


	/**
	 * Write distinct instruction and limit instructions.
	 * @param plan
	 * @param selectBuffer
	 */
	protected void writeStartLimitClause(SearchPlan plan, Clause selectBuffer) {
		if (plan.isDistinct()){
			selectBuffer.append(" DISTINCT ");
		} 

	}



	protected void writeResultProjection (SearchPlan plan, Clause queryClause){

		if (plan.isCountOnly()){

			queryClause.append("COUNT(*)"); // TODO count on a primary key column or index column

		} 
		//		else if (!plan.groups().isEmpty() || !plan.projectionOperators().isEmpty()){ // if this is a projection criteria 
		//
		//			// add groupBy
		//			for (Iterator<DataColumnModel> it = plan.groups().iterator();it.hasNext();){
		//				DataColumnModel cm = it.next();
		//
		//				// get hardname
		//				dialect().writeQueryHardname(queryClause, cm.getName());
		//
		//				if (it.hasNext()){
		//					queryClause.append(" , ");
		//				}
		//			}
		//
		//			if (!plan.groups().isEmpty() && !plan.projectionOperators().isEmpty()){
		//				queryClause.append(" , ");
		//			}
		//
		//			// add operators
		//			for (Iterator<ProjectionOperator> it = plan.projectionOperators().iterator() ;it.hasNext();){
		//				translateAggregationOperator(it.next() , queryClause);
		//
		//				if (it.hasNext()){
		//					queryClause.append(' ').append(',').append(' ');
		//				}
		//			}
		//
		//		}
		else {
			// this a normal reading criteria

			// run thought the fields. 
			if (plan.getResultColumns().isEmpty()){
				queryClause.append(" * ");
			} else {
				for (Iterator<ResultColumnDefinition> it = plan.getResultColumns().iterator() ; it.hasNext(); ){
					ResultColumnDefinition definition = it.next();

					DBColumnModel hcm = dataBaseMapper.getTableColumnModel(definition.getName());


					switch (definition.getFunction()){
					case VALUE:
						dialect().writeQueryHardname(queryClause , hcm.getName());
						break;
					case COUNT:
						queryClause.append("COUNT (");
						dialect().writeQueryHardname(queryClause , hcm.getName());
						queryClause.append(")");
						break;
					case AVERAGE:
						queryClause.append("AVG (");
						dialect().writeQueryHardname(queryClause , hcm.getName());
						queryClause.append(")");
						break;
					case MAXIMUN:
						queryClause.append("MAX (");
						dialect().writeQueryHardname(queryClause , hcm.getName());
						queryClause.append(")");
						break;
					case MINIMUM:
						queryClause.append("MIN (");
						dialect().writeQueryHardname(queryClause , hcm.getName());
						queryClause.append(")");
						break;
					case SUM:
						queryClause.append("SUM (");
						dialect().writeQueryHardname(queryClause , hcm.getName());
						queryClause.append(")");
						break;
					default:
						throw new IllegalStateException(definition.getFunction() + " is not a recognized " + ResultFunction.class);
					}

					if (definition.getAlias() != null){
						queryClause.append(" AS ").append(definition.getAlias());
					}

					if (it.hasNext()){
						queryClause.append(' ').append(',').append(' ');
					}

				}
			}
		}
	}



	protected void writeEndLimitClause(SearchPlan plan, Clause selectBuffer){
		// no-op
	}



	protected void writeAliasSeparator(Clause queryBuffer){
		queryBuffer.append(" AS ");
	}

	protected void writeAlias(Clause queryBuffer , String alias){
		queryBuffer.append(alias);
	}	

	private void sweepJunctionCriterions(List<JunctionCriterion> criterions ,Collection<Criterion> criterias ){

		for (Iterator<Criterion> it = criterias.iterator(); it.hasNext();){
			Criterion c = (Criterion)it.next();
			if (c instanceof JunctionCriterion){
				JunctionCriterion junction = (JunctionCriterion)c;
				if ( junction.getSubCriteria() != null){
					criterions.add(junction);
					sweepJunctionCriterions( criterions, 
							junction.getSubCriteria().constraints().reduce().criterias()
							);
				}
			}
		}

	}

	protected void writeFromJoinClause(SearchPlan plan, Clause clause){

		// FROM ClAUSE
		clause.append(" FROM ");

		boolean isFirst = true;
		for (TableRelation relation : plan.getSourceDataSets()){
			// source table
			if (isFirst && relation.getSourceTableModel() != null){
				DBTableModel tb = relation.getSourceTableModel();

				dataBaseDialect.writeEnclosureHardname(clause, tb.getName());

				if (relation.getSourceAlias() != null){
					writeAliasSeparator(clause); 
					writeAlias(clause, relation.getSourceAlias());
				}
			}

	

			// target table
			if (relation.getTargetTableModel() != null){
				
				// join operator
				switch (relation.getRelationOperator()){
				case INNER_JOIN:
					clause.append(" INNER JOIN ");
					break;
				case OUTTER_LEFT_JOIN:
					clause.append(" LEFT JOIN ");
					break;
				case OUTTER_RIGHT_JOIN:
					clause.append(" RIGHT JOIN ");
					break;
				}
				
				DBTableModel tb = relation.getTargetTableModel();

				dataBaseDialect.writeEnclosureHardname(clause, tb.getName());

				if (relation.getTargetAlias() != null){
					writeAliasSeparator(clause); 
					writeAlias(clause, relation.getTargetAlias());
				}
				
				// on clause
				clause.append(" ON ");

				writeOut(clause, relation.getRelationConstraint() , new LinkedList<ValueHolder>());
			}

			
			
			isFirst = false;
		}

	}



	protected void writeOrderByClause(SearchPlan plan, Clause clause){

		// do not write order by if its a count query
		if (plan.isCountOnly()){
			return;
		}

		Collection<OrderConstraint> list = plan.getOrdering();


		// simple ordering
		if (!list.isEmpty()){
			clause.append(" ORDER BY ");

			// result table level
			for (Iterator<OrderConstraint> it = list.iterator(); it.hasNext(); ){

				
				writeOrdeByClauseElement(plan, clause, it.next());

				if (it.hasNext()){
					clause.append(", ");
				}
			}
		}

	}

	protected void writeOrdeByClauseElement(SearchPlan plan, Clause clause, OrderConstraint orderConstraint){

		if (orderConstraint.isAlias()){
			
			this.writeAlias(clause, orderConstraint.getColumnName().getDesignation());

		} else {
			DBColumnModel cm = this.dataBaseMapper.getTableColumnModel(orderConstraint.getColumnName());
			
			dialect().writeQueryHardname(clause, cm.getName());

		}
		clause.append(orderConstraint.isDescendant()?" desc":" asc");
	}


	protected void writeGroupByClause(SearchPlan plan, Clause queryBuffer){


		//GROUP BY

		Collection<ColumnGroupConstraint> aggregationGroups = plan.getGroups();
		

		if (!aggregationGroups.isEmpty()){

			queryBuffer.append(" GROUP BY ");

			for (Iterator<ColumnGroupConstraint> it = aggregationGroups.iterator(); it.hasNext();){
				QualifiedName name = it.next().getColumnName();

				dialect().writeQueryHardname(queryBuffer,  this.dataBaseMapper.getTableColumnModel(name).getName());

				if (it.hasNext()){
					queryBuffer.append(", ");
				}
			}

		}


	}

	protected void writeWhereClause(SearchPlan plan, Clause queryBuffer, Collection<ValueHolder> params  ){
		// WHERE CLAUSE
		// if not constraint exists, no clause exists
		if ( plan.getConstraints().isEmpty()){
			return;
		}

		Clause whereClause =  new Clause();

		translateCriteriaToWhereClause(whereClause,params, plan.getConstraints());

		if (whereClause.length()>2){
			queryBuffer.append(" WHERE ");
			queryBuffer.append(whereClause);
		}

	}



	//	protected void translateFieldInQueryCriteriaToEmptySetInClause(Clause criteriaBuffer,FieldInSetCriterion criterion,StorableEntityModel model){
	//		// [NOT] IN ( foo ) 
	//		// where foo is return nothing select 
	//		// made using range(0)
	//		EntityCriteria<?> emptyCriteria =  EntityCriteriaBuilder.search(criteria.getTargetClass()).limit(0).all();
	//
	//		emptyCriteria.setKeyOnly(true);
	//
	//		RDBMSDataSetCriteriaInterpreter ci = dialect().newCriteriaInterpreter(criteria, reader);
	//
	//		StringBuilder queryBuffer = new StringBuilder();
	//
	//		criteriaBuffer.append(' ');
	//		dialect().writeQueryHardname(criteriaBuffer, model.fieldModel(criterion.getFieldName()).getHardName());
	//
	//		if (!criterion.isIncluded()){
	//			queryBuffer.append(" NOT ");
	//		}
	//
	//		queryBuffer.append(" IN (");
	//		queryBuffer.append(ci.translateRetrive(plan).toString());
	//		queryBuffer.append(")");
	//
	//		criteriaBuffer.append(queryBuffer);
	//	}

	//	protected void translateFieldInQueryCriteriaToWhereClause(Clause criteriaBuffer,FieldInSetCriterion criterion,StorableEntityModel  model){
	//
	//		// [NOT] IN ( Select field FROM table ... ) 
	//		EntityCriteria<?> c = (EntityCriteria<?>)criterion.valueHolder().getValue();
	//		c.setDistinct(true);
	//
	//		RDBMSDataSetCriteriaInterpreter ci = dialect().newCriteriaInterpreter(c, reader);
	//
	//		StringBuilder queryBuffer = new StringBuilder();
	//
	//		criteriaBuffer.append(' ');
	//		dialect().writeQueryHardname(criteriaBuffer, model.fieldModel(criterion.getFieldName()).getHardName());
	//
	//
	//		if (!criterion.isIncluded()){
	//			queryBuffer.append(" NOT ");
	//		}
	//
	//		RetriveDataBaseCommand r = ci.translateRetrive(plan);
	//
	//		queryBuffer.append(" IN (")
	//		.append(r.toString()) // TODO merge param values 
	//		.append(")");
	//
	//		criteriaBuffer.append(queryBuffer);
	//	}


	protected  void translateCriteriaToWhereClause(Clause criteriaBuffer, Collection<ValueHolder> paramsValues, DataSetConstraint constraint ){

		if (constraint instanceof ColumnValueConstraint){
			ColumnValueConstraint f = (ColumnValueConstraint) constraint;

			ColumnValueLocator left = f.getLeftValuelocator();

			ColumnValueLocator right = f.getRightValueLocator();

			CriterionOperator op = f.getOperator();

			// the left side must be a ColumnNameValueLocator

			ColumnNameValueLocator n = (ColumnNameValueLocator) left;

			// TODO use as model for ValueHolder
			DBColumnModel columnModel = this.dataBaseMapper.getTableColumnModel(n.getName());

			if (CriterionOperator.CONTAINS.equals(op) || 
					CriterionOperator.STARTS_WITH.equals(op) || 
					CriterionOperator.ENDS_WITH.equals(op) ){

				// TODO determine case sensitivity
				writeLikeClause(criteriaBuffer, false, left, op, right, paramsValues, columnModel);
				
			}  else if (CriterionOperator.IN.equals(op)){
				// is required the right locator to be a MultipleValueLocator

				if (right instanceof MultipleValueLocator) {

					MultipleValueLocator multiple = (MultipleValueLocator) right;

					writeColumnValueLocator(criteriaBuffer, left, paramsValues , columnModel);

					if (op.isNegated()){
						criteriaBuffer.append(" NOT");
					} 
					criteriaBuffer.append(" IN (");


					for (Iterator<ColumnValueLocator> it  = multiple.getValues().iterator(); it.hasNext(); ){
						writeColumnValueLocator(criteriaBuffer, it.next(), paramsValues , columnModel);

						if (it.hasNext()){
							criteriaBuffer.append(", ");
						}
					}

					criteriaBuffer.append(") ");


				} else if (right instanceof IntervalValueLocator){

					IntervalValueLocator interval = (IntervalValueLocator) right;

					criteriaBuffer.append("(");

					if (interval.getStart() != null){

						writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);

						if (op.isNegated()){
							criteriaBuffer.append(" < ");
						} else {
							criteriaBuffer.append(" >= ");
						}

						writeColumnValueLocator(criteriaBuffer, interval.getStart(), paramsValues, columnModel);

					}

					if (interval.getEnd()!=null){

						if (op.isNegated()){
							if (interval.getStart() != null){
								criteriaBuffer.append(" OR ");
							}

							writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);

							criteriaBuffer.append(" > ");

						} else {
							if (interval.getStart() != null){
								criteriaBuffer.append(" AND ");
							}
							writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);

							criteriaBuffer.append(" <= ");
						}

						writeColumnValueLocator(criteriaBuffer, interval.getEnd(), paramsValues, columnModel);


					}

					criteriaBuffer.append(") ");

				} else {
					throw new IllegalStateException("wrong right operand for IN clause .(" + right.getClass() + ")");
				}

			} else if (CriterionOperator.IS_NULL.equals(op)){
				writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);

				if (op.isNegated()){
					criteriaBuffer.append(" IS NOT NULL ");
				} else {
					criteriaBuffer.append(" IS NULL ");
				}

				// do not write the right side.


			} else {
				writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);

				if (op.isNegated()){

					if (CriterionOperator.GREATER_THAN.equals(op)){
						criteriaBuffer.append(" <= ");
					} else if (CriterionOperator.GREATER_THAN_OR_EQUAL.equals(op)){
						criteriaBuffer.append(" < ");
					}  else if (CriterionOperator.LESS_THAN.equals(op)){
						criteriaBuffer.append(" >= ");
					}  else if (CriterionOperator.LESS_THAN_OR_EQUAL.equals(op)){
						criteriaBuffer.append(" > ");
					} else if (CriterionOperator.EQUAL.equals(op)){
						criteriaBuffer.append(" <> ");
					}  else {
						throw new RuntimeException("Unkown operator " + op);
					} 
				} else { // positive operators

					if (CriterionOperator.GREATER_THAN.equals(op)){
						criteriaBuffer.append(" > ");
					} else if (CriterionOperator.GREATER_THAN_OR_EQUAL.equals(op)){
						criteriaBuffer.append(" >= ");
					}  else if (CriterionOperator.LESS_THAN.equals(op)){
						criteriaBuffer.append(" < ");
					}  else if (CriterionOperator.LESS_THAN_OR_EQUAL.equals(op)){
						criteriaBuffer.append(" <= ");
					}  else if (CriterionOperator.EQUAL.equals(op)){
						criteriaBuffer.append(" = ");
					}  else {
						throw new RuntimeException("Unkown operator " + op);
					}
				}

				writeColumnValueLocator(criteriaBuffer, right, paramsValues, columnModel);

			}



		}else if (constraint instanceof LogicConstraint){

			writeOut(criteriaBuffer ,(LogicConstraint) constraint , paramsValues );
		} 
	}
	
	protected void writeLikeClause(Clause criteriaBuffer,
			boolean caseSensitive,  ColumnValueLocator left, CriterionOperator op, ColumnValueLocator right, Collection<ValueHolder> paramsValues, DBColumnModel columnModel) {
		
		writeColumnValueLocator(criteriaBuffer, left, paramsValues, columnModel);

		if (op.isNegated()){
			criteriaBuffer.append(" NOT");
		}
		criteriaBuffer.append(" LIKE ");

		writeColumnValueLocator(criteriaBuffer, right, paramsValues, columnModel);
	}

	/**
	 * @param clause 
	 * @param paramsValues 
	 * @param relationConstraint
	 */
	private void writeOut(Clause clause, LogicConstraint logicConstraint, Collection<ValueHolder> paramsValues) {
		
		
		final LogicConstraint simple = logicConstraint.simplify();
		
		
		if (!simple.isEmpty()){
			
			final List<DataSetConstraint> constraints = simple.getConstraints();
			clause.append("(");

			for (Iterator<DataSetConstraint> it = constraints.iterator(); it.hasNext(); ){
				translateCriteriaToWhereClause(clause, paramsValues, it.next());

				if (it.hasNext()){
					clause.append(" ").append(logicConstraint.getOperator().toString()).append(" ");
				}
			}

			clause.append(")");
		}

	}
	/**
	 * @param criteriaBuffer
	 * @param model 
	 * @param left
	 */
	protected final void writeColumnValueLocator(Clause criteriaBuffer, ColumnValueLocator locator , Collection<ValueHolder> values, DBColumnModel model) {
		if (locator instanceof ColumnNameValueLocator){
			ColumnNameValueLocator c = (ColumnNameValueLocator) locator;
			// print the column name

			writeQualifiedName(criteriaBuffer, c.getName());

		} else if (locator instanceof ExplicitValueLocator){
			ExplicitValueLocator c = (ExplicitValueLocator) locator;

			criteriaBuffer.append("? ");

			values.add(new ExplicitValueHolder(c.getValue(), model));

		} else if (locator instanceof ParameterValueLocator){
			ParameterValueLocator c = (ParameterValueLocator) locator;

			criteriaBuffer.append("? ");

			values.add(new ParameterizedValueHolder(c.getParameterName(), model));


		}
	}



	/**
	 * @param criteriaBuffer
	 * @param name
	 */
	private void writeQualifiedName(Clause criteriaBuffer, QualifiedName name) {

		dialect().writeQueryHardname(criteriaBuffer, dataBaseMapper.getTableColumnModel(name).getName());
	}



}
