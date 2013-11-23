/**
 * 
 */
package org.middleheaven.io.filerepository;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.io.repository.ManagedFileRepositoryProvider;



/**
 * 
 */
class MapFileRepositoryService extends AbstractFileRepositoryService {

	
	private Map<String, ManagedFileRepositoryProvider> providers = new HashMap<String, ManagedFileRepositoryProvider>();
	
	/**
	 * 
	 * Constructor.
	 */
	public MapFileRepositoryService(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerProvider(ManagedFileRepositoryProvider provider) {
		providers.put(provider.getScheme().toLowerCase(), provider);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unRegisterProvider(ManagedFileRepositoryProvider provider) {
		providers.remove(provider.getScheme().toLowerCase());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected ManagedFileRepositoryProvider resolveProviderByScheme(String scheme) {
		return providers.get(scheme.toLowerCase());
	}







}
