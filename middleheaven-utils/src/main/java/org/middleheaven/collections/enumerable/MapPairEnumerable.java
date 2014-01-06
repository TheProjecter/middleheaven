package org.middleheaven.collections.enumerable;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.middleheaven.collections.KeyValuePair;
import org.middleheaven.collections.TransformedIterator;
import org.middleheaven.util.function.Function;

class MapPairEnumerable<K, V> extends AbstractEnumerable<KeyValuePair<K,V>> implements PairEnumerable<K, V> , FastCountEnumerable{

	
	private Map<K, V> other;

	@SuppressWarnings("unchecked")
	public MapPairEnumerable(Map<? extends K, ? extends V> other){
		this.other = (Map<K, V>) other;
	}
	
	@Override
	public Iterator<KeyValuePair<K, V>> iterator() {
		return TransformedIterator.transform(other.entrySet().iterator(), new Function<KeyValuePair<K,V> , Map.Entry<K,V>>(){

			@Override
			public KeyValuePair<K, V> apply(final Entry<K, V> entry) {
				return new KeyValuePair<K,V>(){

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
	
	public int size(){
		return other.size();
	}


}
