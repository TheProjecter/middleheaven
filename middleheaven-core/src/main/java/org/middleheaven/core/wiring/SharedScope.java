package org.middleheaven.core.wiring;

import java.util.HashMap;
import java.util.Map;

public final class SharedScope extends AbstractScopePool {

	
	private final Map<Key , Object> objects = new HashMap<Key , Object>();
	
	public SharedScope (){}
	
	@Override
	public  Object getInScope(ResolutionContext context, WiringQuery query, Resolver resolver) {
		Key key = Key.keyFor(query.getContract().getName(), query.getParams());
		
		Object obj = objects.get(key);
		
		if (obj==null){
			obj = resolver.resolve(context, query);
			objects.put(key,obj);
			this.fireObjectAdded(obj);
		}
		return obj;
	}


	@Override
	public void remove(Object object) {
		if (objects.values().remove(object)){
			this.fireObjectRemoved(object);
		};
	}


	@Override
	public void clear() {
		this.objects.clear();
	}




}
