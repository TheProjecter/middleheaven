package org.middleheaven.ui;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.PropertyBagProxyHandler;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

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
		
		Method m = Introspector.of(GenericUIComponent.class).inspect().methods()
			.named(delegator.getName())
			.withParametersType(delegator.getInvoked().getParameterTypes())
			.retrive();
		
		if (m!=null){
			return m.invoke(original,args);
		} else if (Property.class.isAssignableFrom(delegator.getInvoked().getReturnType())){
			final String name = delegator.getInvoked().getName();
			Property p = adicionalProperties.get(name);
			if (p == null){
				p = ValueProperty.writable(StringUtils.firstLetterToLower(resolveName(name)), delegator.getInvoked().getReturnType());
				adicionalProperties.put(name, p);
			}
			return p;
		}
		return null;
		
	}
	
	private String resolveName(String name){
		int pos = name.indexOf("Property");
		if (pos < 0){
			return name.substring(3);
		} else {
			return name.substring(3, pos);
		}
	}
	public String toString(){
		return original.toString();
	}

}
