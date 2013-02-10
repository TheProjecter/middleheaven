/**
 * 
 */
package org.middleheaven.util.property;

import java.beans.PropertyChangeListener;

import org.middleheaven.util.function.Block;

/**
 * 
 */
public interface Property<T> {

	public T get();
	public Property<T> set(T value);
	
	public String getName();
	
	public boolean isReadOnly();
	
	public boolean hasValue();
	
	public void addListener(PropertyChangeListener listener);
	public void removeListener(PropertyChangeListener listener);
	
	public Property<T> onChange(Block<T> trigger);
	
	/**
	 * @return
	 */
	public Class<?> getValueType();
}
