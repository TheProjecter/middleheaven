/**
 * 
 */
package org.middleheaven.persistance;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 */
class DefaultDataService implements DataService {

	
	private final List<DataStoreProvider>  providers = new LinkedList<DataStoreProvider>();
	
	private final Map<DataStoreName, DataStore> stores = new HashMap<DataStoreName, DataStore>();
	private final Map<DataStoreSchemaName, DataStoreSchema> schemas = new HashMap<DataStoreSchemaName, DataStoreSchema>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addProvider(DataStoreProvider provider) {
		providers.add(provider);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeProvider(DataStoreProvider provider) {
		providers.remove(provider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataStore getDataStore(DataStoreName name) throws DataStoreNotFoundException {
		
		if (!stores.containsKey(name)){
			
			DataStore store = null;
			
			for (DataStoreProvider p : providers){
				if (p.isProviderDataStore(name)){
					store = p.getDataStore(name);
					break;
				}
			}
			
			stores.put(name, store);
			
			if (store == null){
				throw new DataStoreNotFoundException();
			}
			
			return store;
		} else {
			return stores.get(name);
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataStoreSchema getDataStoreSchema(DataStoreSchemaName name) throws DataStoreSchemaNotFoundException {
		try {
			
			if (!schemas.containsKey(name)){
				
				DataStore store = this.getDataStore(name.getDataStoreName());
				
				DataStoreSchema schema = store.getDataStoreSchema(name);
				
				schemas.put(name, schema);
				
				if (schema == null){
					throw new DataStoreSchemaNotFoundException();
				}
				
				return schema;
				
			} else {
				return schemas.get(name);
			}
			
			
			
		} catch (DataStoreNotFoundException e){
			throw new DataStoreSchemaNotFoundException(e);
			
		}
		
		
	}

	/**
	 * 
	 */
	public void close() {
		// no-op
	}








}
