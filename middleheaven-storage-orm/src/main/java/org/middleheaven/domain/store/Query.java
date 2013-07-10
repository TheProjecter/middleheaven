/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.domain.query.QueryResult;
import org.middleheaven.util.criteria.ReadStrategy;

/**
 * 
 */
public interface Query<T> {

	
	public void setParameter(String parameterName, Object parameterValueType);
	
	public QueryResult<T> execute();
	
	public QueryResult<T> execute(ReadStrategy readStrategy);
}
