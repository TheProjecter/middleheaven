package org.middleheaven.collections;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.middleheaven.util.function.Mapper;

public class MapPairEnumerable<K, V> extends AbstractEnumerable<Pair<K,V>> implements PairEnumerable<K, V> {

	
	private Map<K, V> other;

	@SuppressWarnings("unchecked")
	public MapPairEnumerable(Map<? extends K, ? extends V> other){
		this.other = (Map<K, V>) other;
	}
	
	@Override
	public Iterator<Pair<K, V>> iterator() {
		return TransformedIterator.transform(other.entrySet().iterator(), new Mapper<Pair<K,V> , Map.Entry<K,V>>(){

			@Override
			public Pair<K, V> apply(final Entry<K, V> entry) {
				return new Pair<K,V>(){

					@Override
					public K getKey() {
						return entry.getKey();
					}

					@Override
					public V getValue() {
						return entry.getValue();
					}
					
				};
			}
			
		});
	}

	@Override
	public Map<K, V> into(Map<K, V> map) {
		
		map.putAll(other);
		
		return map;
	}

}
