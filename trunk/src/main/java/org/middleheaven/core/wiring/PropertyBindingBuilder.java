package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;

public class PropertyBindingBuilder<T>  {

	protected Binding<T> binding;
	protected EditableBinder binder;
	protected String name;
	protected T object;
	
	PropertyBindingBuilder (EditableBinder binder , Binding<T> binding){
		this.binder = binder;
		this.binding = binding;
		this.binding.addAnnotation(Name.class);
		binder.addBinding(binding);
	}

	public PropertyBindingBuilder<T> named (String name){
		this.name = name;
		if (this.object != null && name!=null){
			((PropertyResolver<T>)binding.getResolver()).setProperty(name , object);
		}
		return this;
	}
	
	public PropertyBindingBuilder<T> in(Class<? extends ScopePool> scope){
		binding.setTargetScope(scope);
		return this;
	}
	
	public PropertyBindingBuilder<T> toInstance(T object){
		this.object = object;
		if (this.object != null && name!=null){
			((PropertyResolver<T>)binding.getResolver()).setProperty(name , object);
		}
		return this;
	} 
	
	public PropertyBindingBuilder<T> toProvider(Class<Provider<T>> type){
		//binding.setResolver(new ProviderResolver(type));
		return this;
	}
	
	public PropertyBindingBuilder<T> annotatedWith(Class<? extends Annotation> type){
		binder.removeBinding(binding);
		binding.addAnnotation(type);
		binder.addBinding(binding);
		return this;
	}
}
