package org.middleheaven.collections;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public final class ParamsMap implements Map<String,String>, Serializable{

	private static final long serialVersionUID = -5704086450957356147L;
	private HashMap<String, String> map;

	public ParamsMap() {
		this.map = new HashMap<String,String>();
	}
	
	/**
	 * 
	 * @param other
	 * @return <code>true</code> if the contents are the same
	 */
	public boolean containsSame(Map<String, String> other) {
		return CollectionUtils.equalContents(this, other);
	}

	public ParamsMap(Map<String,String> other) {
		this.map = new HashMap<String,String>(other);
	}

	
	public ParamsMap setParam(String name, String value){
		this.put(name, value);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object clone() {
		return new ParamsMap(this.map);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsKey(Object arg0) {
		return map.containsKey(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(Object arg0) {
		return map.containsValue(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<java.util.Map.Entry<String, String>> entrySet() {
		return map.entrySet();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object arg0) {
		return map.equals(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String get(Object arg0) {
		return map.get(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return map.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<String> keySet() {
		return map.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	public String put(String arg0, String arg1) {
		return map.put(arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	public void putAll(Map<? extends String, ? extends String> arg0) {
		map.putAll(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String remove(Object arg0) {
		return map.remove(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return map.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return map.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<String> values() {
		return map.values();
	}

	


	
}
