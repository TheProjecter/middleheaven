package org.middleheaven.domain.store;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.util.identity.Identity;

abstract class AbstractDomainStoreDecorator implements DomainStore {

	/**
	 * Implement this method to retrieve the correct original EntityStore instance. 
	 * @return
	 */
	protected abstract DomainStore original();
	
	@Override
	public void addStorageListener(DomainStoreListener listener) {
		this.original().addStorageListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria) {
		return original().createQuery(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Query<T> retriveNameQuery(String name, Class<T> type) {
		return original().retriveNameQuery(name, type);
	}


	@Override
	public Identity getIdentityFor(Object object) {
		return this.original().getIdentityFor(object);
	}

	@Override
	public <T> void remove(T obj) {
		this.original().remove(obj);
	}

	@Override
	public <T> void remove(EntityCriteria<T> criteria) {
		this.original().remove(criteria);
	}

	@Override
	public void removeStorageListener(DomainStoreListener listener) {
		this.original().removeStorageListener(listener);
	}

	@Override
	public <T> T store(T obj) {
		return original().store(obj);
	}
	
	

}
