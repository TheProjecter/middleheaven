package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;

import org.middleheaven.reflection.Reflector;

public class ScopeBindingBuilder<T> {

	protected Binding binding;
	protected EditableBinder binder;

	
	ScopeBindingBuilder (EditableBinder binder , Binding binding){
		this.binder = binder;
		this.binding = binding;
	}
	
	public ScopeBindingBuilder<T> to(Class<? extends Annotation> scopeClass){
		binder.removeBinding(binding);
		this.binding.setSourceType(Reflector.getReflector().reflect(scopeClass));
		binder.addBinding(binding);
		return this;
	}

}
