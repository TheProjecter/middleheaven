package org.middleheaven.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.core.reflection.PropertyBagProxyHandler;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.ui.property.Property;

class GenericUIComponentProxyHandler extends PropertyBagProxyHandler {

	private GenericUIComponent original;
	private Class<?> uiClass;
	
	private Map<String, Property> adicionalProperties = new HashMap<String, Property>();

	public GenericUIComponentProxyHandler(GenericUIComponent object,Class<?> uiClass) {
		this.original = object;
		this.uiClass = uiClass;
		this.setProperty("type", uiClass);
		
	}

	@Override
	public Object invoke(Object proxy, Object[] args, MethodDelegator delegator)	throws Throwable {
		
		MethodHandler m = Introspector.of(GenericUIComponent.class).inspect().methods()
			.named(delegator.getName())
			.withParametersType(delegator.getInvoked().getParameterTypes())
			.retrive();
		
		final Class<?> returnType = delegator.getInvoked().getReturnType();
		if (m!=null){
			return m.invoke(original,args);
		} 
		
		if (Iterable.class.isAssignableFrom(returnType) || Set.class.isAssignableFrom(returnType) || Collection.class.isAssignableFrom(returnType)){
			return Collections.emptySet();
		} else if (List.class.isAssignableFrom(returnType)){
			return Collections.emptySet();
		}
		return null;
		
	}

	public String toString(){
		return original.toString();
	}

}
