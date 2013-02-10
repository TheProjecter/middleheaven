package org.middleheaven.domain.store;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.query.Query;
import org.middleheaven.util.criteria.ReadStrategy;
import org.middleheaven.util.identity.Identity;

/**
 * Controls the entity store state.
 */
public interface DomainStoreManager {

	/**
	 * Creates a query based on a criteria , a read strategy for a storage unit
	 * @param <T> the object type.
	 * @param criteria the criteria
	 * @param strategy the strategy.
	 * @param unit the storage unit
	 * @return the query.
	 */
	public  <T> Query<T> createQuery(final EntityCriteria<T> criteria,
			final ReadStrategy strategy, final StorageUnit unit);

	/**
	 * 
	 * @param object
	 * @return
	 */
	public  Identity getIdentityFor(Object object);
	
	/**
	 * 
	 * @return the domain model for the managed store.
	 */
	public DomainModel getDomainModel();

	/**
	 * Remove the object from the storageUnit
	 * @param <T> he object type
	 * @param obj
	 * @param unit
	 */
	public  <T> void remove(T obj, StorageUnit unit);

	public  <T> T store(T obj, final StorageUnit unit);

	public  void commit(StorageUnit unit);

	public  void roolback(StorageUnit unit);
	
	
	/**
	 * Register a storage listener.
	 * @param listener
	 */
	public void addStorageListener(DomainStoreListener listener);
	
	/**
	 * Unregister a storage listener.
	 * @param listener
	 */
	public void removeStorageListener(DomainStoreListener listener);

	

}