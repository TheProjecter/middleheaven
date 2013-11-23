/**
 * 
 */
package org.middleheaven.core.metaclass;


/**
 * An interface for a object with properties that can be read and written.
 */
public interface MetaBean {

	/**
	 * 
	 * @return the associated {@link MetaClass}
	 */
	public MetaClass getMetaClass();
	
	/**
	 * 
	 * @param name the property name
	 * @return the property value.
	 */
	public Object get(String name);
	
	/**
	 * 
	 * @param name the property name
	 * @param value property value.
	 */
	public void set(String name, Object value);
}
