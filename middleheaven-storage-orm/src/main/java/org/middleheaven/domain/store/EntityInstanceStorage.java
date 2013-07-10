package org.middleheaven.domain.store;

import java.util.Collection;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.query.QueryResult;
import org.middleheaven.util.criteria.ReadStrategy;

/**
 * Abstraction for a object mapping mechanism. 
 */
public interface EntityInstanceStorage {

	/**
	 * Include new objects in the storage
	 * @param obj Collection of objects to include
	 */
	public void insert(Collection<EntityInstance> obj);
	
	/**
	 * Update the state of objects in the storage
	 * @param obj Collection of objects to update
	 */
	public void update(Collection<EntityInstance> obj);
	
	/**
	 * Removes the objects, in the collection, from the storage
	 */
	public void remove(Collection<EntityInstance> obj);
	
	/**
	 * Removes all object, that match the criteria, from the store
	 */
	public <T> void remove(EntityCriteria<T> criteria);
	
	/**
	 * Creates a store-specific <code>Query</code> object using a <code>Criteria</code>
	 * The store is not immediately consult for results. The consult only occurs when one the Query 
	 * methods is invoked. Repeated invocations of the same query method may return different results as
	 * data in the store is updated.
	 * 
	 * @param <T> return object type
	 * @param criteria filter to be applied to the bulk data
	 * @param <code>ReadStrategy</code> read strategy
	 * @return
	 */
	public <T> QueryResult<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy);

	
	/**
	 * Register the controlling state manager
	 * @param stateManager the associated state manager
	 */
	public void setStorableStateManager(DomainStoreManager stateManager);



}
