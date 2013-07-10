/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.query.QueryExecuter;
import org.middleheaven.domain.query.QueryParametersBag;
import org.middleheaven.domain.query.QueryResult;
import org.middleheaven.util.criteria.ReadStrategy;

/**
 * 
 */
public class ParametrizedCriteriaQuery<T> implements Query<T> {

	private QueryParametersBag queryParametersBag = new QueryParametersBag();
	
	private EntityCriteria<T> criteria;
	private QueryExecuter executer;

	public ParametrizedCriteriaQuery (final EntityCriteria<T> criteria, QueryExecuter executer){
		this.criteria = criteria;
		this.executer = executer;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParameter(String parameterName, Object parameterValueType) {
		queryParametersBag.setParameter(parameterName, parameterValueType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryResult<T> execute() {
		return execute(ReadStrategy.defaultStrategy());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryResult<T> execute(ReadStrategy readStrategy) {
		return new LayzEntityCriteriaQueryResult<T>(criteria, executer , queryParametersBag , readStrategy);
	}

}
