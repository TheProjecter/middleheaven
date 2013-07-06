package org.middleheaven.cache;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ExtendableCacheService implements CacheService {

	private final Map<String ,CacheRegion > regions = new HashMap<String ,CacheRegion>();
	private final Deque<CacheManager> managers = new LinkedList<CacheManager>();
	
	public ExtendableCacheService(){}
	
	public void addManager(CacheManager manager){
		managers.addFirst(manager);
	}
	
	public void removeManager(CacheManager manager){
		managers.remove(manager);
	}
	
	private CacheManager resolveManager(CacheSpecification specification){
		
		for (CacheManager manager : managers){
			if (manager.canUse(specification)){
				return manager;
			}
		}
		return null;
	}

	@Override
	public void register(String regionName, CacheSpecification specification) {
		
		final CacheManager resolveManager = resolveManager(specification);
		
		if (resolveManager == null){
			throw new CacheException("No manager could be found for specification type " + specification.getClass());
		}

		regions.put(regionName,resolveManager.createRegion(regionName,specification));
	}

	@Override
	public void unRegister(String regionName) {
		regions.remove(regionName);
	}

	@Override
	public Cache getCache(String regionName) {
		CacheRegion region = regions.get(regionName);
		if (region==null){
			throw new CacheRegionNotFoundException();
		}
		
		return region.getCache();
	}
	
	 

}
