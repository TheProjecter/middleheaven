package org.middleheaven.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * And abastract implementation of a {@link BoundMap}.
 * @param <K> the key type
 * @param <V> the value type.
 */
public abstract class AbstractBoundMap<K,V> implements BoundMap<K,V> {

	
	private Map<K, V> direct;
	private Map<V, K> inverse;
	private BoundMap<V, K> reversedMap;

	
	public AbstractBoundMap(Map<K,V> direct, Map<V,K> reversed, BoundMap<V,K> reversedMap){
		this.direct = direct;
		this.inverse = reversed;
		this.reversedMap = reversedMap;
	}
	
	@Override
	public BoundMap<V, K> reversed() {
		if(reversedMap==null){
			this.reversedMap = createInverseMap(inverse, direct,this);
		}
		return reversedMap;
	}
	
	protected Map<K, V> direct(){
		return direct;
	}
	
	protected Map<V, K> inverse(){
		return inverse;
	}
	
	protected abstract BoundMap<V, K> createInverseMap(Map<V, K> reversed,Map<K, V> direct, BoundMap<K, V> reversedMap);

	@Override
	public void clear() {
		direct.clear();
		inverse.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return direct.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return inverse.containsKey(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return direct.entrySet();
	}

	@Override
	public V get(Object key) {
		return direct.get(key);
	}

	@Override
	public boolean isEmpty() {
		return direct.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		// TODO the modification of this set reflect on the modification of both
		// direct and reversed
		return direct.keySet();
	}

	@Override
	public V put(K key, V value) {
		
		if(direct.containsKey(key)){
			inverse.remove(direct.get(key));
		}
		
		if (inverse.containsKey(value)){
			direct.remove(inverse.get(value));
		}

		inverse.put(value, key);
		return direct.put(key, value);
	
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		 for (Entry<? extends K, ? extends V> entry : map.entrySet()){
			 this.put(entry.getKey(), entry.getValue());
		 }
	}

	@Override
	public V remove(Object key) {
		V value = direct.remove(key);
		inverse.remove(value);
		return value;
	}

	@Override
	public int size() {
		return direct.size();
	}

	@Override
	public Collection<V> values() {
		// TODO modification on this collection
		// propagate both direct and reverse
		return direct.values();
	}

}
