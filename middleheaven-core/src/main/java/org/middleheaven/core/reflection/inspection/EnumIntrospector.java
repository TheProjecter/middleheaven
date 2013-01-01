package org.middleheaven.core.reflection.inspection;

import java.lang.reflect.InvocationTargetException;

import org.middleheaven.core.reflection.ReflectionException;

public class EnumIntrospector<E> extends ClassIntrospector<E>{

	
	EnumIntrospector(Class<E> type) {
		super(type);
	}

	public static <E extends Enum> EnumIntrospector of(Class<E> type) {
		return new EnumIntrospector<E>(type);
	}
	
	
	public E[] getValues(){

		try {
			return (E[]) this.inspect()
					.staticMethods()
					.named("values")
					.retrive()
					.invoke(null, new Object[0]);
			
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e, this.getIntrospected());
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e, this.getIntrospected());
		} catch (InvocationTargetException e) {
			throw ReflectionException.manage(e, this.getIntrospected());
		}
		
	}
}
