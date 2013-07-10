package org.middleheaven.storage.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.middleheaven.collections.ComposedMapKey;
import org.middleheaven.collections.DualMapKey;
import org.middleheaven.domain.store.EntityInstance;
import org.middleheaven.domain.store.EntityInstanceStorage;
import org.middleheaven.util.identity.Identity;

public class StoreQuerySession {

	private final static ThreadLocal<StoreQuerySession> sessions = new ThreadLocal<StoreQuerySession>();
	
	public static StoreQuerySession getInstance(EntityInstanceStorage dataStorage) {
		StoreQuerySession session =  sessions.get();
		if ( session == null){
			session = new StoreQuerySession(dataStorage);
			sessions.set(session);
		}
		return session;
	}
	
	private final Map<ComposedMapKey, EntityInstance> cache = new HashMap<ComposedMapKey, EntityInstance>();
	private final EntityInstanceStorage dataStorage;
	private final AtomicInteger counter = new AtomicInteger();
	
	public StoreQuerySession(EntityInstanceStorage dataStorage){
		this.dataStorage = dataStorage;
	}
	
	public void open() {
		counter.incrementAndGet();
	}

	public void close() {
		if (counter.decrementAndGet()==0){
			sessions.set(null);
		}
	}
	
	public void put(EntityInstance to) {
		if (to.getIdentity() != null){
			cache.put(DualMapKey.of(to.getClass().getSimpleName(),to.getIdentity()), to);	
		}
		
	}
	
	public EntityInstance get(Class<?> type, Identity identity){
		if (identity == null || type == null){
			return null;
		}
		return cache.get(DualMapKey.of(type.getName(),identity));

	}
	






}
