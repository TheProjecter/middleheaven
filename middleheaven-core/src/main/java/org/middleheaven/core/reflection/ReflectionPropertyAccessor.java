package org.middleheaven.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.function.Maybe;

public final class ReflectionPropertyAccessor extends ReflectionFieldHandler implements PropertyHandler {

	private Method assessor;
	private Method modifier;
	private boolean modifyByField = false;
	private boolean readOnly = false;
	private boolean writeOnly = false;

	public ReflectionPropertyAccessor(Class<?> type, String fieldName) {
		super(type,fieldName);
		load();
	}

	private static class TypeData implements Comparable<TypeData> {
		Class<?> type;
		
		
		protected TypeData(Class<?> type) {
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
		Method assessor;
		Method modifier;
		
		public Method getAssesor() {
			return assessor;
		}
		public Method getModifier() {
			return modifier;
		}
		public void setAssesor(Method assesor) {
			this.assessor = assesor;
		}
		public void setModifier(Method modifier) {
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
	
	
	protected void load(){

		try {
			// find corresponding field


			Field[] fields = type.getDeclaredFields();
			for (Field f : fields){
				if(f.getName().equalsIgnoreCase(name)){
					field = f;
					break;
				}
			}


			// find assessor and modifier
			
			
			Map<TypeData ,  MethodsData> map = new TreeMap<TypeData ,  MethodsData>();
			
			for (Method method : type.getMethods()){
				
				if (method.getParameterTypes().length==1 && method.getName().equalsIgnoreCase("set" + name)){
					
					method.setAccessible(true);
					
					TypeData td = new TypeData(method.getParameterTypes()[0]);
					
					
					td.getMethodsData(map).modifier = method;
					
				} else if (method.getParameterTypes().length==0 && (method.getName().equalsIgnoreCase("get" + name) ||  
						method.getName().equalsIgnoreCase("is" + name)) ){
					
				
					method.setAccessible(true);
					
					TypeData td = new TypeData(method.getReturnType());
					
					
					td.getMethodsData(map).assessor = method;
					
				} 
				
				
			}
			
			if (map.size() == 1){
				final MethodsData data = map.entrySet().iterator().next().getValue();
				this.modifier = data.modifier;
				this.assessor = data.assessor;
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
				this.modifier = data.modifier;
				this.assessor = data.assessor;
			}
			
			
			
			if (this.modifier == null && this.assessor == null){
				throw new PropertyNotFoundException( name ,  type.getName());
			}
			
			if (field != null){
				if (modifier==null){
					// use modification  by field
					modifyByField = true;
				}
			} else {
				this.writeOnly = assessor == null && this.modifier != null;
				this.readOnly = assessor != null && this.modifier == null;
			}
		} catch (SecurityException e) {
			throw new ReflectionException(e);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(e);
		} 
	}

	public Class<?> getValueType(){
		if (assessor != null){
			return assessor.getReturnType();
		} else {
			return modifier.getParameterTypes()[0];
		}
	}

	public Object getValue(Object target) throws ReflectionException{
		if (this.writeOnly){
			throw new WriteOnlyPropertyException();
		}
		try {
			if (assessor!=null){
				return assessor.invoke(target, new Object[0]);
			} else if (field !=null){
				field.setAccessible(true);
				return field.get(target);
			} else {
				throw  new PropertyNotFoundException( this.name ,  target.getClass().getName());
			}
		} catch (Exception e) {
			throw ReflectionException.manage(e, this.type);
		} 
	}
	
	public void  setValue(Object target, Object value ){
		if (this.readOnly){
			throw new ReadOnlyPropertyException(this.name);
		}
		try {
			
			if( value == null && getValueType().isPrimitive()){
				return; // cannot set a primitive to null. TODO throw exception ?
			} else {
				value = TypeCoercing.coerce(value,getValueType());
				if (modifier != null){
					
					modifier.invoke(target, new Object[]{value});
				} else if (modifyByField && field!=null){
					field.setAccessible(true);
					field.set(target,value);
				} // else is read only. not an exception
			}
		
		}catch (Exception e) {
			throw ReflectionException.manage(e, this.type);
		} 
	}

	public <A extends Annotation> boolean isAnnotadedWith(Class<A> annotationClass) {
		return (field!=null && field.isAnnotationPresent(annotationClass)) || 
		(assessor!=null && assessor.isAnnotationPresent(annotationClass)) || 
		(modifier!=null && modifier.isAnnotationPresent(annotationClass));
	}

	public <A extends Annotation> Maybe<A> getAnnotation(Class<A> annotationClass) {
		A a=null;
		if (field != null){
			a = field.getAnnotation(annotationClass);
		} 
		
		if (a==null && assessor != null){
			a = assessor.getAnnotation(annotationClass);
		}
		return Maybe.of(a);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadable() {
		return this.assessor != null || this.field != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isWritable() {
		return this.modifier != null || this.field != null;
	}
}

