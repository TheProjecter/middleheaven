package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

public class SharedScope implements ScopePool {

	
	private final Map<Key , Object> objects = new HashMap<Key , Object>();
	
	
	@Override
	public <T> T scope(WiringSpecification<T> query, Resolver<T> resolver) {
		Key key = Key.keyFor(query.getContract(),query.getSpecifications());
		
		@SuppressWarnings("unchecked") T obj = (T)objects.get(key);
		if (obj==null){
			obj = resolver.resolve(query);
			objects.put(key,obj);
		}
		return obj;
	}

}
