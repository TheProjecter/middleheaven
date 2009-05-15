package org.middleheaven.core.wiring;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.middleheaven.util.collections.ParamsMap;


public class ScoopingModel {

	private List<Class<?>> scopes = new LinkedList<Class<?>>();
	Map<String,String> params = new ParamsMap();
	
	public ScoopingModel(){

	}

	public void addScope(Class<?> scope) {
		this.scopes.add(scope);
	}
	
	public void addParam(String name, String value) {
		this.params.put(name,value);
	}
	
	public void addParams(Map<String,String> other) {
		this.params.putAll(other);
	}
	
	public <T> void addToScope(EditableBinder binder, Object object) {
		@SuppressWarnings("unchecked") T t = (T) object;
		@SuppressWarnings("unchecked") Class<T> type = (Class<T>) object.getClass();

		if (scopes.isEmpty()){
			// add to default
			scopes.add(DefaultScope.class);
		} 
		
		for (Class<?> scope : scopes){
			Binding binding = new Binding();
			binding.setStartType(type);
			binding.setTargetScope(scope);
			binding.setResolver(new InstanceResolver<T>(t));
		
			
			WiringSpecification<T> spec = WiringSpecification.search(type,params);

			binder.getScopePool(binding).add(spec, t);
		}



	}

	



}