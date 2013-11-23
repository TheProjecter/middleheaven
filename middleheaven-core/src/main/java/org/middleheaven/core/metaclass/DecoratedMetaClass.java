package org.middleheaven.core.metaclass;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.collections.enumerable.EnhancedArrayList;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.reflection.PropertyHandler;
import org.middleheaven.core.reflection.PropertyNotFoundException;

public class DecoratedMetaClass implements MetaClass {
	
	private MetaClass original;
	
	private Map<String, PropertyHandler> acessors = new HashMap<String, PropertyHandler>();
	
	public DecoratedMetaClass (MetaClass original){
		this.original = original;	
	
	}
	
	/**
	 * 
	 * @param acessor
	 */
	public void addProperty(PropertyHandler acessor){
		this.acessors.put(acessor.getName(), acessor);
	}
	
	@Override
	public PropertyHandler getPropertyAcessor(String name) throws PropertyNotFoundException {
		PropertyHandler pa = this.acessors.get(name);
		
		if (pa == null){
			return this.acessors.get(name);
		}
		return pa;
	}

	@Override
	public Enumerable<PropertyHandler> getProperties() {
		EnhancedArrayList<PropertyHandler> allProperties = new EnhancedArrayList<PropertyHandler>(original.getProperties());
		
		allProperties.addAll(this.acessors.values());
		
		return allProperties;
	}

	@Override
	public MetaBean newInstance() {
		MetaBean originalBean = original.newInstance();
		
		return new DecoratedReflectionBean(originalBean, original, this);
	}

	@Override
	public String getSimpleName() {
		return original.getSimpleName();
	}

	@Override
	public String getName() {
		return original.getName();
	}

	@Override
	public boolean containsProperty(String name) {
		return this.original.containsProperty(name) || this.acessors.containsKey(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotation) {
		return (A) original.getAnnotation(annotation);
	}

}
