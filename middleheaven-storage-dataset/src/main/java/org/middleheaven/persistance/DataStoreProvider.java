/**
 * 
 */
package org.middleheaven.persistance;

import org.middleheaven.storage.dataset.mapping.DatasetRepositoryModel;

/**
 * 
 */
public interface DataStoreProvider {

	/**
	 * Obtains the {@link DataStore} that corresponds with the required name. 
	 * If this {@link DataStoreProvider} can not provide the required data store a {@link DataStoreNotFoundException} is throw.
	 * To avoid receiving this exception consider use {@link #isProviderDataStore(DataStoreName)}.
	 * 
	 * @param name the {@link DataStoreName} identifying the required data store.
	 * @return the {@link DataStore} that corresponds with the required name.
	 * @throws {@link DataStoreNotFoundException} if the required data store is not found.
	 * @throws {@link IllegalArgumentException} if the name is <code>null</code>.
	 */
	public DataStore getDataStore (DataStoreName name) throws DataStoreNotFoundException;
	
	
	/**
	 * Indicates if this {@link DataStoreProvider} can provide the required data store.
	 * @param name the {@link DataStoreName} identifying the required data store.
	 * @return <code>true</code> if this provider can provide the required data store, <code>false</code> otherwise.
	 */
	public boolean isProviderDataStore (DataStoreName name);


	/**
	 * @param name
	 * @param dataSetModel
	 */
	public void registerDataStore(DataStoreName name, DatasetRepositoryModel dataSetModel);
	

	
	
}
