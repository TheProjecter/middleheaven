/**
 * 
 */
package org.middleheaven.collections;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.util.StripedLock;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.function.Mapper;
/**
 * 
 */
public class WeakConcurrentKeyMap<K, V> implements Map<K, V> {

	private final Map<K,WeakReference<V>> support = new HashMap<K,WeakReference<V>>();
	private final StripedLock stripedLock = new StripedLock();
	private Function<V, K> function;
	
	public WeakConcurrentKeyMap(Function<V, K> function){
		this.function = function;
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
			WeakReference<V> ref = support.get(key);
			if (ref == null || ref.get() == null){
				ref = new WeakReference<V>(function.apply(key));
				support.put(key, ref);
			}
			return ref.get();
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
		throw new UnsupportedOperationException();
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
			WeakReference<V> ref = support.remove(key);
			return ref == null ? null : ref.get();
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
		return  new HashSet<java.util.Map.Entry<K, V>>(TransformedCollection.transform(support.entrySet(), new Mapper<java.util.Map.Entry<K, V>, java.util.Map.Entry<K, WeakReference<V>> >(){

			@Override
			public java.util.Map.Entry<K, V> apply(final java.util.Map.Entry<K, WeakReference<V>> obj) {
				return new java.util.Map.Entry<K, V>(){

					@Override
					public K getKey() {
						return obj.getKey();
					}

					@Override
					public V getValue() {
						return obj.getValue().get();
					}

					@Override
					public V setValue(V arg0) {
						throw new UnsupportedOperationException();
					}
					
				};
			}
			
		})); 
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<V> values() {
		return  TransformedCollection.transform(support.values(), new Mapper<V, WeakReference<V>>(){

			@Override
			public V apply(WeakReference<V> obj) {
				return obj.get();
			}
			
		}); 
	}
	

}
