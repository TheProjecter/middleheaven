/**
 * 
 */
package org.middleheaven.ui.property;

import java.io.Serializable;


/**
 * 
 */
public class ValueProperty<T extends Serializable> extends AbstractProperty<T> {

	private T value;
	private boolean readOnly = false;
	
	public static <X extends Serializable> Property<X> writable(String propertyName, Class<X> type ){
		return new ValueProperty<X>(propertyName , null, type, false);
	}
	
	public static <X extends Serializable> Property<X> writable(String propertyName, X value ){
		return new ValueProperty<X>(propertyName , value, (Class<X>) value.getClass(), false);
	}
	
	public static <X extends Serializable> Property<X> readOnly(String propertyName, X value){
		return new ValueProperty<X>(propertyName , value, (Class<X>) value.getClass(), true);
	}
	
	protected ValueProperty(String propertyName, T value , Class<T> type, boolean readOnly){
		super(propertyName, type);
		this.value = value;
		this.readOnly = readOnly;
	}
	
	public T get(){
		return value;
	}
	
	public Property<T> set(T value){
		
		if (!readOnly && valuesDiffer(this.value, value)){
			
			T oldValue = this.value;
			this.value = value;
			
			fireChange( oldValue, value);
			
		}
		
		return this;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasValue() {
		return value != null;
	}
	
	public String toString(){
		return this.getName() + "=" + value;
	}


}
