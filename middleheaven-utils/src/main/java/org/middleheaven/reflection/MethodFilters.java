package org.middleheaven.reflection;

import java.lang.reflect.Modifier;

import org.middleheaven.util.function.Predicate;


/**
 * Utility class for use with ReflectionUtils to filter methods in a class
 *
 */
public class MethodFilters implements Predicate<ReflectedMethod> {

	/**
	 * 
	 * @return methods that are public, non-static and that do not 
	 * conform to the get/set convention
	 */
	public static MethodFilters publicInstanceNonProperty(){
		return new MethodFilters(){

			@Override
			public Boolean apply(ReflectedMethod method) {
				final String name = method.getName();
				final int modifiers = method.getModifiers();
				return Boolean.valueOf(Modifier.isPublic(modifiers) && !(name.startsWith("get") || name.startsWith("set") || name.startsWith("is")));
			}
		};
	}

	public static MethodFilters publicProperties(){
		return new MethodFilters(){

			@Override
			public Boolean apply(ReflectedMethod method) {
				final String name = method.getName();
				final int modifiers = method.getModifiers();
				return Boolean.valueOf(Modifier.isPublic(modifiers) && !name.equals("getClass") && (name.startsWith("get") || name.startsWith("set") || name.startsWith("is")));
			}
		};

	}

	public static MethodFilters all(){
		return new MethodFilters();
	}

	private MethodFilters(){

	}


	@Override
	public Boolean apply(ReflectedMethod method) {
		return Boolean.TRUE;
	}

}
