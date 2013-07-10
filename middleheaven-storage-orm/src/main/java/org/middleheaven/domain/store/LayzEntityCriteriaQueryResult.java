/**
 * 
 */
package org.middleheaven.domain.store;

import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.query.QueryParametersBag;
import org.middleheaven.domain.query.QueryResult;
import org.middleheaven.domain.query.QueryExecuter;
import org.middleheaven.util.criteria.ReadStrategy;



/**
 * 
 */
public class LayzEntityCriteriaQueryResult<T> implements QueryResult<T>{

	private EntityCriteria<T> criteria;
	private QueryExecuter queryExecuter;
	private QueryParametersBag queryParametersBag;
	private ReadStrategy readStrategy;
	
	/**
	 * Constructor.
	 * @param criteria
	 * @param strategy
	 * @param queryExecuter
	 * @param queryParametersBag 
	 * @param readStrategy 
	 */
	public LayzEntityCriteriaQueryResult(EntityCriteria<T> criteria,QueryExecuter queryExecuter, QueryParametersBag queryParametersBag, ReadStrategy readStrategy) {
		this.criteria = criteria;
		this.queryExecuter = queryExecuter;
		this.queryParametersBag= queryParametersBag;
		this.readStrategy = readStrategy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T fetchFirst() {
		Collection<T> result = limit(0, 1).fetchAll();
		if (result.isEmpty()){
			return null;
		} else {
			return result.iterator().next();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<T> fetchAll() {
		return this.queryExecuter.retrive(criteria, readStrategy, queryParametersBag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long count() {
		return this.queryExecuter.count(criteria, queryParametersBag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.queryExecuter.existsAny(criteria, queryParametersBag);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryResult<T> limit(int startAt, int maxCount) {
		return new LayzEntityCriteriaQueryResult<T>(criteria.setRange(startAt, maxCount), queryExecuter, queryParametersBag, readStrategy);
	}

}
