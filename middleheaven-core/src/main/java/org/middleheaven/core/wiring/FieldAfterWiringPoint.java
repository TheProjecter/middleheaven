package org.middleheaven.core.wiring;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.middleheaven.core.reflection.ReflectionException;


/**
 * Used a {@link Field} as a {@link AfterWiringPoint}.
 */
public class FieldAfterWiringPoint implements AfterWiringPoint{

	private Field field;
	private WiringSpecification specs;
	
	/**
	 * 
	 * Constructor.
	 * @param f the field that objects will be wired to..
	 * @param specs the wiring specification. 
	 */
	public FieldAfterWiringPoint(Field f, WiringSpecification specs) {
		super();
		this.field = f;
		this.specs = specs;
	}

	public List<WiringSpecification> getSpecifications(){
		return Collections.singletonList(specs);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return field.hashCode();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals(Object other){
		return (other instanceof FieldAfterWiringPoint) && this.field.equals(((FieldAfterWiringPoint)other).field);
	}
	
	
	public String toString(){
		return this.field.toString();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public <T> T writeAtPoint(InstanceFactory factory, T object){
		if(object == null){
			return null;
		}
		
		field.setAccessible(true);

		WiringTarget fieldTarget = new FieldWiringTarget(field, object);
		
		Object value = factory.getInstance(WiringQuery.search(specs).on(fieldTarget));
		
		if (value==null && specs.isRequired()){
			throw new BindingException("Cannot find instance to bind at " + field.getDeclaringClass().getName() +"." + field.getName());
		}
		
		if(!field.getType().isInstance(value)){
			throw new BindingException(value.getClass().getName() + " can not be assigned to " + field.getType().getName());
		}
		try {	
			field.set( object, value );
		} catch (Exception e) {
			ReflectionException.manage(e, object.getClass());
		}
		
		return object;
	}

	@Override
	public boolean isRequired() {
		return specs.isRequired();
	}
}
