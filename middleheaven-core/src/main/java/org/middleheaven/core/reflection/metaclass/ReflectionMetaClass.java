package org.middleheaven.core.reflection.metaclass;

import java.lang.annotation.Annotation;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.PropertyNotFoundException;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.util.collections.Enumerable;

/**
 * Wraps a {@link java.lang.Class} in a {@link MetaClass}.
 */
public class ReflectionMetaClass implements MetaClass{


	private Class<?> type;

	/**
	 * 
	 * @param type the reflect class.
	 */
	public ReflectionMetaClass(Class<?> type){
		this.type = type;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public PropertyAccessor getPropertyAcessor(String name) {
		PropertyAccessor pa = Introspector.of(type).inspect().properties().named(name).retrive();

		if (pa == null){
			throw new PropertyNotFoundException("Property " + name + " not found in " + this.getName());
		}
		return pa;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<PropertyAccessor> getProperties() {
		return Introspector.of(type).inspect().properties().retriveAll();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public MetaBean newInstance() {
		return new ReflectionBean(Introspector.of(type).newInstance(), this);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getSimpleName() {
		return this.type.getSimpleName();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return type.getName();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsProperty(String name) {
		return  Introspector.of(type).inspect().properties().named(name).retrive() != null;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		return (other instanceof ReflectionMetaClass) && this.type.equals( ((ReflectionMetaClass) other).type);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode(){
		return this.type.hashCode();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotation) {

		return type.getAnnotation(annotation);

	}

	/**
	 * 	
	 * {@inheritDoc}
	 */
	public String toString(){
		return this.type.toString();
	}


}
