package org.middleheaven.reflection.impl;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.reflection.NotReadablePropertyException;
import org.middleheaven.reflection.NotWritablePropertyException;
import org.middleheaven.reflection.PropertyNotFoundException;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedField;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.reflection.ReflectedProperty;
import org.middleheaven.reflection.ReflectionException;
import org.middleheaven.util.Maybe;
import org.middleheaven.util.coersion.TypeCoercing;

public final class JavaPropertyAccessor implements ReflectedProperty {

	private ReflectedMethod accessor;
	private ReflectedMethod modifier;
	private boolean modifyByField = false;
	private ReflectedClass<?> type;
	private String name;
	private ReflectedField field;
	
	public static JavaPropertyAccessor forTypeAndName(ReflectedClass<?> type, String propertyName){
		try {
			
			JavaPropertyAccessor result = new JavaPropertyAccessor(type, propertyName);
			
			// find corresponding field
			for (ReflectedField f : type.getDeclaredFields()){
				if(f.getName().equalsIgnoreCase(propertyName)){
				    result.field = f;
					break;
				}
			}

			// find assessor and modifier
			
			Map<TypeData ,  MethodsData> map = new TreeMap<TypeData ,  MethodsData>();
			
			for (ReflectedMethod method : type.getMethods()){
				
				final Enumerable<ReflectedParameter> parameters = method.getParameters();
				if (parameters.size()==1 && method.getName().equalsIgnoreCase("set" + propertyName)){
					
					TypeData td = new TypeData(method.getParameters().getFirst().getType());
					
					td.getMethodsData(map).setModifier(method);
					
				} else if (parameters.size() ==0 && (method.getName().equalsIgnoreCase("get" + propertyName) ||  
						method.getName().equalsIgnoreCase("is" + propertyName)) ){

					TypeData td = new TypeData(method.getReturnType());
					td.getMethodsData(map).setAssesor(method);
					
				} 
				
				
			}
			
			if (map.size() == 1){
				final MethodsData data = map.entrySet().iterator().next().getValue();
				result.modifier = data.getModifier();
				result.accessor = data.getAssesor();
			} else if(map.size() > 1) {
				// take the more priority with two methods
				MethodsData data = null;
				for (Iterator<Entry<TypeData,MethodsData>> it = map.entrySet().iterator(); it.hasNext();){
					Entry<TypeData,MethodsData> entry = it.next();
					if (entry.getValue().count() == 2) {
						data = entry.getValue();
						break;
					}
				}
				
				if (data == null){
					data = map.entrySet().iterator().next().getValue();
				}
				result.modifier = data.getModifier();
				result.accessor = data.getAssesor();
			}
			
			
			
			if (result.modifier == null && result.accessor == null){
				throw new PropertyNotFoundException( propertyName ,  type.getName());
			}
			
			if (result.field != null){
				if (result.modifier==null){
					// use modification  by field
					result.modifyByField = true;
				}
			} 
			
			return result;
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} 
	}

	private JavaPropertyAccessor(ReflectedClass<?> type, String name) {
		this.type = type;
		this.name = name;
	}

	public ReflectedClass<?> getDeclaringClass(){
		return type;
	}
	
	public String getName() {
		return name;
	}

	private static class TypeData implements Comparable<TypeData> {
		ReflectedClass<?> type;
		
		
		protected TypeData(ReflectedClass<?> type) {
			super();
			this.type = type;
		}

		public boolean equals (Object other){
			return other instanceof TypeData && this.type.equals(((TypeData) other).type);
		}
		
		public int hashCode (){
			return type.getName().hashCode();
		}

		/**
		 * @param map
		 */
		public MethodsData getMethodsData(Map<TypeData, MethodsData> map) {
			MethodsData md = map.get(this);
			if (md == null){
				md = new MethodsData();
				map.put(this, md);
			}
			return md;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(TypeData o) {
			if (o.type.isAssignableFrom(this.type)){
				if (this.type.isAssignableFrom(o.type)){
					return 0;
				} else {
					return -1;
				}
			} else {
				return 1;
			}
		}
	}
	
	private static class MethodsData {
		private ReflectedMethod assessor;
		private ReflectedMethod modifier;
		
		public ReflectedMethod getAssesor() {
			return assessor;
		}
		public ReflectedMethod getModifier() {
			return modifier;
		}
		public void setAssesor(ReflectedMethod assesor) {
			this.assessor = assesor;
		}
		public void setModifier(ReflectedMethod modifier) {
			this.modifier = modifier;
		}
		/**
		 * @return
		 */
		public int count() {
			int count = 0;
			if (assessor != null){
				count++;
			}
			
			if (modifier != null){
				count++;
			}
			
			return count;
		}
	}
	
	public ReflectedClass<?> getValueType(){
		if (accessor != null){
			return accessor.getReturnType();
		} else {
			return modifier.getParameters().getFirst().getType();
		}
	}

	public Object getValue(Object target) throws ReflectionException{
		if (!this.isReadable()){
			throw new NotReadablePropertyException();
		}
		try {
			if (accessor!=null){
				return accessor.invoke(target, new Object[0]);
			} else if (field !=null){
				return field.get(target);
			} else {
				throw  new PropertyNotFoundException( this.name ,  target.getClass().getName());
			}
		} catch (Exception e) {
			throw ReflectionException.manage(e);
		} 
	}
	
	public void setValue(Object target, Object value ){
		if (!this.isWritable()){
			throw new NotWritablePropertyException(this.name);
		}
		try {
			
			if( value == null && getValueType().isPrimitive()){
				throw new IllegalArgumentException("Cannot set a primitive to null");
			} else {
				value = TypeCoercing.coerce(value,getValueType().getReflectedType());
				if (modifier != null){
					
					modifier.invoke(target, new Object[]{value});
				} else if (modifyByField && field!=null){
					field.set(target,value);
				} // else is read only. not an exception
			}
		
		}catch (Exception e) {
			throw ReflectionException.manage(e);
		} 
	}

	public <A extends Annotation> boolean isAnnotationPresent(Class<A> annotationClass) {
		return (field!=null && field.isAnnotationPresent(annotationClass)) || 
		(accessor!=null && accessor.isAnnotationPresent(annotationClass)) || 
		(modifier!=null && modifier.isAnnotationPresent(annotationClass));
	}

	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass) {
		Maybe<A> annot = Maybe.absent();
		
		if (field != null){
			annot = field.getAnnotation(annotationClass);
		} 
		
		if (annot.isAbsent() && accessor != null){
			annot = Maybe.of(accessor.getAnnotation(annotationClass));
		}
		return annot;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadable() {
		return this.accessor != null || this.field != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return this.modifier != null || this.field != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Object target, Object value) {
		if (!isWritable()){
			throw new RuntimeException("not writable");
		}
		if (modifier != null){
			 modifier.invoke(target, value);
		} else {
			 field.set(target, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(Object target) {
		if (!isReadable()){
			throw new RuntimeException("not readable");
		}
		if (accessor != null){
			return accessor.invoke(target);
		} else {
			return field.get(target);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<Annotation> getAnnotations() {
		Enumerable<Annotation> all = Enumerables.emptyEnumerable();
		if (this.accessor != null){
			all = all.concat(this.accessor.getAnnotations());
		}
		if (this.field != null){
			all = all.concat(this.field.getAnnotations());
		}
		if (this.modifier != null){
			all = all.concat(this.modifier.getAnnotations());
		}
		return all;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getModifiers() {
		return this.accessor != null ? this.accessor.getModifiers() : this.field.getModifiers();
	}
	
	public String toString(){
		return name;
	}
}

