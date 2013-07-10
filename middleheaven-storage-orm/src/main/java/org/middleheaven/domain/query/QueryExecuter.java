package org.middleheaven.domain.query;

import java.util.Collection;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.util.criteria.ReadStrategy;


public interface QueryExecuter {

	
	public <T> Collection<T> retrive(EntityCriteria<T> query, ReadStrategy readStrategy, QueryParametersBag queryParametersBag);
	public <T> long count(EntityCriteria<T> query, QueryParametersBag queryParametersBag);
	public <T> boolean existsAny(EntityCriteria<T> query, QueryParametersBag queryParametersBag);
}
