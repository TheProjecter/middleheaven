package org.middleheaven.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class EnhancedMapAdapter<K,V> extends AbstractEnumerableAdapter<Pair<K,V>> implements Map<K,V> {

	
	private Map<K, V> original;

	protected EnhancedMapAdapter(Map<K,V> original){
		this.original = original;
	}
	
	/**
	 * 
	 * @param other
	 * @return <code>true</code> if the contents are the same
	 */
	public boolean containsSame(Map<K, V> other) {
		return CollectionUtils.equalContents(this, other);
	}

	@Override
	public boolean isEmpty() {
		return original.isEmpty();
	}

	public V put(K key, V value) {
		return original.put(key, value);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		original.putAll(m);
	}

	public V remove(Object key) {
		return original.remove(key);
	}

	@Override
	public int size() {
		return original.size();
	}

	@Override
	public Iterator<Pair<K,V>> iterator() {
		final Iterator<Map.Entry<K,V>> originalIt = original.entrySet().iterator();
		return new Iterator <Pair<K,V>>(){

			@Override
			public boolean hasNext() {
				return originalIt.hasNext();
			}

			@Override
			public Pair<K,V> next() {
				final Entry<K, V> next = originalIt.next();
				
				return new Pair<K,V>(){

					@Override
					public K getKey() {
						return next.getKey();
					}

					@Override
					public V getValue() {
						return next.getValue();
					}
					
				};
			}

			@Override
			public void remove() {
				originalIt.remove();
			}
			
		};
	}

	public String toString(){
		return this.original.toString();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.original.containsKey(key);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsValue(Object value) {
		return this.original.containsValue(value);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public V get(Object key) {
		return this.original.get(key);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.original.clear();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<K> keySet() {
		return this.original.keySet();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<V> values() {
		return this.original.values();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.original.entrySet();
	}

}
