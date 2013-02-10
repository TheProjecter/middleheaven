package org.middleheaven.domain.store;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.services.ServiceRegistry;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.transactions.TransactionService;

/**
 * {@link DomainStoreService} implementations that uses a simple Map to map names with configuration.
 */
public class HashDomainStoreService implements DomainStoreService {

	private static final ThreadLocal<Map<String,DomainStore>> threadStore = new ThreadLocal<Map<String,DomainStore>>(); 
	
	private final Map<String , Info > infos = new HashMap<String, Info> ();
	private String firstEntityName;
	
	/**
	 * Auxiliary structure.
	 */
	private static class Info {

		private DomainStoreManager domainStoreManager;
		
		public Info (DomainStoreManager domainStoreManager) {
			this.domainStoreManager = domainStoreManager;
		}
		
	}
	
	/**
	 * 
	 * Constructor.
	 */
	public HashDomainStoreService(){
		
	}

	@Override
	public DomainStore getStore() {
		if(firstEntityName == null){
			throw new IllegalStateException("No entity store is registered.Please register an EntityStore first.");
		}
		return getStore(firstEntityName);
	}


	@Override
	public DomainStore getStore(String name) {
		
		Map<String,DomainStore> stores = threadStore.get();
		
		if (stores == null){
			stores = new HashMap<String,DomainStore>();
			threadStore.set(stores);
		}
		
		DomainStore store = stores.get(name);
	
		if (store == null ){
			Info info = infos.get(name);
			if (info == null){
				throw new IllegalStateException("No entity store registered under the name " + name);
			}
			
			TransactionService ts = ServiceRegistry.getService(TransactionService.class);
			DomainStoreManager manager =  info.domainStoreManager;
			store = new SessionAwareDomainStore(manager);
			
			ts.enlistResource((SessionAwareDomainStore)store);
			
			stores.put(name, store);
		}
		return store;
	}


	@Override
	public void registerStore(String name, DomainStoreManager domainStoreManager) {
		if(infos.isEmpty()){
			this.firstEntityName = name;
		}
		this.infos.put(name, new Info(domainStoreManager));
	}

	@Override
	public void unRegisterStore(String name) {
		this.infos.remove(name);
	}

	@Override
	public void unRegisterAll() {
		this.infos.clear();
	} 
	
	
}
