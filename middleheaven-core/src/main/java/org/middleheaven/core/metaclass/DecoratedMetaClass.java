package org.middleheaven.core.metaclass;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.PropertyNotFoundException;
import org.middleheaven.reflection.ReflectedProperty;

public class DecoratedMetaClass implements MetaClass {
	
	private MetaClass original;
	
	private Map<String, ReflectedProperty> acessors = new HashMap<String, ReflectedProperty>();
	
	public DecoratedMetaClass (MetaClass original){
		this.original = original;	
	
	}
	
	/**
	 * 
	 * @param acessor
	 */
	public void addProperty(ReflectedProperty acessor){
		this.acessors.put(acessor.getName(), acessor);
	}
	
	@Override
	public ReflectedProperty getPropertyAcessor(String name) throws PropertyNotFoundException {
		ReflectedProperty pa = this.acessors.get(name);
		
		if (pa == null){
			return this.acessors.get(name);
		}
		return pa;
	}

	@Override
	public Enumerable<ReflectedProperty> getProperties() {
		return original.getProperties().concat(Enumerables.asEnumerable(this.acessors.values()));
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
