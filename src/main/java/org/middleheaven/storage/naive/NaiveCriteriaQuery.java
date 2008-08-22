package org.middleheaven.storage.naive;

import java.util.Collection;

import org.middleheaven.storage.Query;
import org.middleheaven.storage.criteria.Criteria;

public class NaiveCriteriaQuery<T> implements Query<T> {

	Criteria<T> criteria;
	NaiveStoreManager manager;
	public NaiveCriteriaQuery(Criteria<T> criteria, NaiveStoreManager manager){
		this.criteria = criteria;
		this.manager = manager;
	}
	
	@Override
	public long count() {
		return list().size();
	}

	@Override
	public T find() {
		return list().iterator().next();
	}

	@Override
	public Collection<T> list() {
		// TODO filter with criteria
		return (Collection<T>)manager.getBulkData(criteria.getTargetClass().getName());
	}

}