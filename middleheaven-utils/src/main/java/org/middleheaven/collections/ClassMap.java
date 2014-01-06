/**
 * 
 */
package org.middleheaven.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.middleheaven.reflection.ReflectedClass;

/**
 * 
 */
public class ClassMap<T> implements Map<String, T>,  Serializable {

	private static final long serialVersionUID = -6830223903725787564L;

	private Map<String, T> map = new HashMap<String, T>();
	
	public ClassMap() {
		
	}

	
	public T put(ReflectedClass<?> type, T value){
		return this.put(type.getName(), value);
	}
	
	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		if (key instanceof Class){
			return map.containsKey(((Class)key).getName());
		} else if (key instanceof String){
			return map.containsKey(key);
		}
		return false;
	}
	

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public T get(Object key) {
		if (key instanceof Class){
			return map.get(((Class)key).getName());
		} else if (key instanceof String){
			return map.get(key);
		}
		return null;
	}

	/**
	 * @param value
	 * @return
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	/**
	 * @return
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<String, T>> entrySet() {
		return map.entrySet();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.Map#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return map.equals(o);
	}


	/**
	 * @return
	 * @see java.util.Map#hashCode()
	 */
	public int hashCode() {
		return map.hashCode();
	}

	/**
	 * @return
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.Map#keySet()
	 */
	public Set<String> keySet() {
		return map.keySet();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public T put(String key, T value) {
		return map.put(key, value);
	}

	/**
	 * @param m
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends String, ? extends T> m) {
		map.putAll(m);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public T remove(Object key) {
		return map.remove(key);
	}

	/**
	 * @return
	 * @see java.util.Map#size()
	 */
	public int size() {
		return map.size();
	}

	/**
	 * @return
	 * @see java.util.Map#values()
	 */
	public Collection<T> values() {
		return map.values();
	}

	
}

