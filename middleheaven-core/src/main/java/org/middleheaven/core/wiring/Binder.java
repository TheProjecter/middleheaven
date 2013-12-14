package org.middleheaven.core.wiring;

import org.middleheaven.reflection.ClassSet;


public interface Binder {


	public <T> BindingBuilder<T> bind(Class<T> type);
	
	public void autobind(ClassSet set);
	
	public <T> PropertyBindingBuilder<T> bindProperty(Class<T> type);

}
