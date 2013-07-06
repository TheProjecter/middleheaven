package org.middleheaven.core.reflection.inspection;


public class EnumIntrospector<E> extends ClassIntrospector<E>{

	
	EnumIntrospector(Class<E> type) {
		super(type);
	}

	public static <E extends Enum> EnumIntrospector of(Class<E> type) {
		return new EnumIntrospector<E>(type);
	}
	
	
	@SuppressWarnings("unchecked")
	public E[] getValues(){

			return (E[]) this.inspect()
					.staticMethods()
					.named("values")
					.retrive()
					.invoke(null);

	}
}
