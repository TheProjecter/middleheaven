package org.middleheaven.domain.query;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.model.EntityModel;

public interface ExecutableQuery<T> extends QueryResult<T>{

	public EntityCriteria<T> getCriteria();

	public EntityModel getModel();
}