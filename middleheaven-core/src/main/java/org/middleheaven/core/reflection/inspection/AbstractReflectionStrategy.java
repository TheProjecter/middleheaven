package org.middleheaven.core.reflection.inspection;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.EnhancedArrayList;
import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.PropertyHandler;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.ReflectionPropertyAccessor;
import org.middleheaven.core.reflection.ReflectionStrategy;
import org.middleheaven.util.StringUtils;

public abstract class AbstractReflectionStrategy implements ReflectionStrategy{

	public PropertyHandler getPropertyAccessor(Class<?> type, String propertyName){
		return new ReflectionPropertyAccessor(this.getRealType(type),propertyName);
	}

	@Override
	public Enumerable<PropertyHandler> getPropertyAccessors(Class<?> type, boolean inherit) throws ReflectionException {
		if (type==null){
			return CollectionUtils.emptyEnumerable();
		}

		Collection<Method> methods = new LinkedList<Method>();

		if (type.isInterface()){
			Reflector.getReflector().getMethods(type).into(methods);
			
			readInterfacesMethods(type, methods);
		} else {
			
			Class<?> topType = this.getRealType(type);
			while ( topType != null && !topType.equals(Object.class)){
				Reflector.getReflector().getMethods(topType).into(methods);
				topType = topType.getSuperclass();
			}
		}

		Set<String> propertyNames = new HashSet<String>();
		EnhancedArrayList<PropertyHandler> result = new EnhancedArrayList<PropertyHandler> ();

		for (Method m : methods){
			if (m.getParameterTypes().length==0){
				if (!m.getName().startsWith("getClass")){
					if (m.getName().startsWith("get") ){
						final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(3));

						if (propertyNames.add(propertyName)){
							result.add(getPropertyAccessor(type, propertyName));
						}

					} else if (m.getName().startsWith("is")){
						final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(2));

						if (propertyNames.add(propertyName)){
							result.add(getPropertyAccessor(type, propertyName));
						}
					} 
				}
			} else if (m.getParameterTypes().length==1){
				if (m.getName().startsWith("set") ){
					final String propertyName = StringUtils.firstLetterToLower(m.getName().substring(3));

					if (propertyNames.add(propertyName)){
						result.add(getPropertyAccessor(type, propertyName));
					}

				} 
			}
		}

		return result;
	}

	private void readInterfacesMethods(Class<?> type, Collection<Method> methods) {
		for (Class<?> i : type.getInterfaces()){
			Reflector.getReflector().getMethods(i).into(methods);
			readInterfacesMethods(i , methods);
		}
	}


}
