/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.LinkedList;
import java.util.ListIterator;

import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.util.coersion.TypeCoercing;

/**
 * 
 */
public class PropertyManagers {
	

	
	private LinkedList<PropertyManager> managers = new LinkedList<PropertyManager>();
	
	/**
	 * Adds a {@link PropertyManager} after another identified by the name.
	 * 
	 * The added  {@link PropertyManager} will be consulted after the named one.
	 * @param managerName
	 * @param manager
	 * @return
	 */
	public boolean addAfter(String managerName, PropertyManager manager){
		for ( ListIterator<PropertyManager> it = managers.listIterator(managers.size() - 1); it.hasPrevious();){
			if (it.previous().getName().equals(managerName)){
				it.add(manager);
				return true;
			}
		}
	
		return false;
	}
	
	
	/**
	 * Adds a {@link PropertyManager} before another identified by the name.
	 * 
	 * The added  {@link PropertyManager} will be consulted before the named one.
	 * @param managerName
	 * @param manager
	 * @return
	 */
	public boolean addBefore(String managerName, PropertyManager manager){
	
		for ( ListIterator<PropertyManager> it = managers.listIterator(); it.hasNext();){
			if (it.next().getName().equals(managerName)){
				it.add(manager);
				return true;
			}
		}
	
		return false;
	}
	
	public void addFirst(PropertyManager manager){
		managers.addFirst(manager);
	}
	
	public void addLast(PropertyManager manager){
		managers.addLast(manager);
	}
	
	public void removePropertyManager(PropertyManager manager){
		managers.remove(manager);
	}
	
	public <T> T getProperty(String key, Class<T> type){
		return getProperty(key, type, null);
	
	}

	public <T> T getProperty(String key, Class<T> type, T defaultValue){
		
		for (PropertyManager manager : managers){
			if (manager.containsProperty(key)){
				// TODO use propertiesEditor instead of cooerce
				return TypeCoercing.coerce(manager.getProperty(key), type);
			} 
		}
		
		return defaultValue;
	}
}
