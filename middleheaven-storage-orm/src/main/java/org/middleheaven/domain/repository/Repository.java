package org.middleheaven.domain.repository;

import org.middleheaven.domain.query.QueryResult;
import org.middleheaven.util.identity.Identity;

/**
 * Logic deposit for object of a certain entity class.
 *
 * @param <E> Repository's entity
 */
public interface Repository<E> {

	/**
	 * Determines the Identity of the given instance
	 * @param instance
	 * @return the identity of the instance or null if it was none.
	 */
	public Identity getIdentityFor(E instance);
	
	/**
	 *  
	 * @return a query that can retrieve all instances in the repository
	 */
	public QueryResult<E> findAll();
	
	/**
	 * 
	 * @param id
	 * @return a query that can retrieve the instance that has the given identity 
	 */
	public QueryResult<E> findByIdentity(Identity id );
	
	/**
	 * 
	 * @param instance
	 * @return a query that can retrieve all instances that are identical to given instance
	 */
	public QueryResult<E> findIdentical(E instance);

	/**
	 * 
	 * @param instance
	 * @return a query that can retrieve all instances that are equal to given instance
	 */
	public QueryResult<E> findEquals(E instance);
	
	/**
	 * Store an instance in the repository giving it an identity. The identified instance
	 * is returned as a result
	 * @param instance to be stored in this repository
	 * @return an instance equal to the stored instance with the repository given identity 
	 */
	public E store(E instance);
	
	/**
	 * Remove the instance from the repository 
	 * @param instance the instance to be removed
	 */
	public void remove(E instance);
	
	/**
	 * Add a repository listener 
	 * @param listener
	 */
	public void addRepositoryListener(RepositoryListener listener);
	
	/**
	 * remove a repository listener
	 * @param listener
	 */
	public void removeRepositoryListener(RepositoryListener listener);
	

}
