package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;



public class PropertyBindingBuilder<T>  {

	protected Binding binding;
	protected EditableBinder binder;
	protected String label;
	protected T object;
	
	PropertyBindingBuilder (EditableBinder binder , Binding binding){
		this.binder = binder;
		this.binding = binding;
		binder.addBinding(binding);
	}

	public PropertyBindingBuilder<T> labeled (String label){
		this.label = label;
		if (this.object != null && label!=null){
			((PropertyResolver)binding.getResolver()).setProperty(label , object);
		}
		return this;
	}
	
	public PropertyBindingBuilder<T> in(Class<? extends Annotation> scope){
		return in(WiringUtils.readScope(scope));
	}
	
	public PropertyBindingBuilder<T> in(String scopeName){
		binding.setScope(scopeName);
		return this;
	}
	
	public PropertyBindingBuilder<T> toInstance(T object){
		this.object = object;
		this.in("shared");
		if (this.object != null && label!=null){
			((PropertyResolver)binding.getResolver()).setProperty(label , object);
		}
		return this;
	} 
	
	public PropertyBindingBuilder<T> toProvider(Class<Provider<T>> type){
		//binding.setResolver(new ProviderResolver(type));
		return this;
	}
	
//	public PropertyBindingBuilder<T> annotatedWith(Class<? extends Annotation> type){
//		binder.removeBinding(binding);
//		binding.addAnnotation(type);
//		binder.addBinding(binding);
//		return this;
//	}
}
