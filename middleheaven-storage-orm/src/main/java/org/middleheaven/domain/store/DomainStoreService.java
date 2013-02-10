package org.middleheaven.domain.store;

import org.middleheaven.core.annotations.Service;

/**
 * A Service to register and retrieve {@link DomainStore}s. 
 */
@Service
public interface DomainStoreService {

	/**
	 * Register a {@link DomainStore} implementation with a given name.
	 * @param name the name of the {@link DomainStore}.
	 * @param domainStoreManager the {@link DomainStoreManager} implementation that will manage the store
	 */
	public void registerStore (String name, DomainStoreManager domainStoreManager);
	
	/**
	 * Unregister the {@link DomainStore} with the given name, if it exists.
	 * @param name the name of the {@link DomainStore}.
	 */
	public void unRegisterStore (String name);
	
	/**
	 * Unregister all previously registered {@link DomainStore}s.
	 */
	public void unRegisterAll();
	
	/**
	 * Return the first registered DomainStore instance allocated to the current transaction. 
	 * @return the first registered DomainStore.  
	 */
	public DomainStore getStore();
	
	/**
	 * Return the registered DomainStore that matches the given name.
	 * @param name the DomainStore name.
	 * @return the DomainStore found, or <code>null</code> if none is found.
	 */
	public DomainStore getStore(String name);
}
