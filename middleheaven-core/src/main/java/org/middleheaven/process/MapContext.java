package org.middleheaven.process;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.middleheaven.collections.TransformedIterator;
import org.middleheaven.collections.enumerable.AbstractEnumerable;
import org.middleheaven.collections.enumerable.FastCountEnumerable;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.function.Function;

public class MapContext extends AbstractAttributeContext {

	EnumMap<ContextScope, ParamMap> contextMap = new EnumMap<ContextScope, ParamMap>(ContextScope.class);


	static class ParamMap extends HashMap<String, Object>{
		
	}
	
	public MapContext(){
		for ( ContextScope scope : ContextScope.values()){
			contextMap.put(scope, new ParamMap());
		}
	}
	
	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		return TypeCoercing.coerce(contextMap.get(scope).get(name), type);
	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		contextMap.get(scope).put(name, value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(ContextScope scope, String name) {
		contextMap.get(scope).remove(name);
	}
	

	public <O> Map<String, O> getScopeMap(ContextScope scope, Class<O> type) {
		
		ParamMap map = contextMap.get(scope);
		if ( map == null){
			return Collections.emptyMap();
		}
		Map<String,O> result = new TreeMap<String,O>();
		for (Map.Entry<String,Object> entry : map.entrySet()){
			result.put(entry.getKey(), TypeCoercing.coerce(entry.getValue(), type));
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopedAttributesResolutionStrategy getScopeAttributeContext(ContextScope scope) {
		return new MapScopedAttributesResolutionStrategy(scope);
	}

	private class MapScopedAttributesResolutionStrategy extends AbstractEnumerable<Attribute> implements ScopedAttributesResolutionStrategy , FastCountEnumerable {

		private ContextScope scope;
		
		public MapScopedAttributesResolutionStrategy (ContextScope scope){
			this.scope = scope;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int size() {
			return contextMap.get(scope).size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isEmpty() {
			return contextMap.get(scope).isEmpty();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<Attribute> iterator() {
			
			return TransformedIterator.transform(contextMap.get(scope).entrySet().iterator(), new Function<Attribute, Map.Entry<String, Object>>(){

				@Override
				public Attribute apply(Entry<String, Object> next) {
					return new ObjectAttribute(next.getKey(), next.getValue());
				}
			
				
			});
			
		}
	
		/**
		 * {@inheritDoc}
		 */
		@Override
		public <T> T getAttribute(String name, Class<T> type) {
			return MapContext.this.getAttribute(scope, name, type);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setAttribute(String name, Object value) {
			MapContext.this.setAttribute(scope, name, value);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void removeAttribute(String name) {
			MapContext.this.removeAttribute(scope, name);
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public ContextScope getScope() {
			return scope;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isReaddable() {
			throw new UnsupportedOperationException("Not implememented yet");
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isWritable() {
			throw new UnsupportedOperationException("Not implememented yet");
		}
	

	}
	
}
