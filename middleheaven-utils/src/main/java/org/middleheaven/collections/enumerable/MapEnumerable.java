/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.middleheaven.collections.KeyValuePair;
import org.middleheaven.collections.TransformedIterator;
import org.middleheaven.util.function.Function;

/**
 * 
 */
class MapEnumerable<K, V> extends AbstractEnumerable<KeyValuePair<K,V>> implements FastCountEnumerable{

	private Map<K, V> map;

	/**
	 * Constructor.
	 * @param map
	 */
	public MapEnumerable(Map<K, V> map) {
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<KeyValuePair<K, V>> iterator() {
		return new TransformedIterator<KeyValuePair<K,V>, Map.Entry<K,V>>(map.entrySet().iterator() , new Function<KeyValuePair<K,V>, Map.Entry<K,V>>(){

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
	
	public int size(){
		return map.size();
	}

}
