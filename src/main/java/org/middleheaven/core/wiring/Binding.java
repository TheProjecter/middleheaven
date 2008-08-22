package org.middleheaven.core.wiring;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class Binding<T> {

	
	private Class<?> startType;
	private Class<? extends Scope> scope;
	private Resolver<T> resolver;
	private Set specifications = new HashSet();
	
	public String toString(){
		return startType.getName() + "+" + specifications.toString();
	}
	protected Class<?> getAbstractType() {
		return startType;
	}
	
	protected void setStartType(Class<?> startType) {
		this.startType = startType;
	}
	
	protected void setResolver(Resolver<T> resolver) {
		this.resolver = resolver;
	}
	
	protected Resolver<T> getResolver() {
		return resolver;
	}

	public Key<T> getKey(){
		return new Key(this.startType, specifications);
	}


	public void addAnnotation(Class<? extends Annotation> type) {
		specifications.add(type);	
	}

	public void setTargetScope(Class<? extends Scope> scope) {
		this.scope = scope;
	}

	public Class<? extends Scope> getScope() {
		return scope;
	}
}