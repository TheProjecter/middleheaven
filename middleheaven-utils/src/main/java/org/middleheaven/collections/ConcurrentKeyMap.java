/**
 * 
 */
package org.middleheaven.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.middleheaven.util.StripedLock;
import org.middleheaven.util.function.Function;
/**
 * 
 */
public class ConcurrentKeyMap<K, V> implements Map<K, V> {

	// TODO size control in memory
	// Weakreference
	private Map<K,V> support = new HashMap<K,V>();
	private Function<V, K> function;
	private final StripedLock stripedLock = new StripedLock();
	
	public ConcurrentKeyMap(Function<V, K> function){
		this.function = function;
	}

	public ConcurrentKeyMap(Function<V, K> function , Map<K,V> support){
		this.function = function;
		this.support = support;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		support.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsKey(Object key) {
		return support.containsKey(key);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(Object value) {
		return support.containsValue(value);
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		return support.equals(o);
	}

	/**
	 * {@inheritDoc}
	 */
	public V get(Object other) {
		K key = (K) other;
		stripedLock.lock(key);
		try{
			V value = support.get(key);
			if (value == null){
				value = function.apply(key);
				support.put(key, value);
			}
			return value;
		} finally{
			stripedLock.unlock(key);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return support.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return support.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<K> keySet() {
		return support.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	public V put(K key, V value) {
		throw new UnsupportedOperationException(" Value are determined by the given function");
	}

	/**
	 * {@inheritDoc}
	 */
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public V remove(Object key) {
		stripedLock.lock(key);
		try{
			return support.remove(key);
		} finally{
			stripedLock.unlock(key);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return support.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return support.entrySet();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<V> values() {
		return support.values();
	}
	

}
