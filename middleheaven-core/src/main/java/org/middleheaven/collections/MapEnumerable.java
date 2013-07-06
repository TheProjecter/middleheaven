/**
 * 
 */
package org.middleheaven.collections;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.middleheaven.util.function.Mapper;

/**
 * 
 */
public class MapEnumerable<K, V> extends AbstractEnumerable<Pair<K,V>> {

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
	public Iterator<Pair<K, V>> iterator() {
		return new TransformedIterator<Pair<K,V>, Map.Entry<K,V>>(map.entrySet().iterator() , new Mapper<Pair<K,V>, Map.Entry<K,V>>(){

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

}
