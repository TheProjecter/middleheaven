package org.middleheaven.collections;

import java.util.Map;

/**
 *A {@link Map} that allows only one unique value per key, permitting a reverse mapping of values to keys.
 * 
 *
 * @param <K> the key type
 * @param <V> the value type.
 */
public interface BoundMap<K,V> extends Map<K,V> {

	/**
	 * The reversed Map where this map keys are he values , and this map values are the keys.
	 * @return the reversed Map.
	 */
	public BoundMap<V,K> reversed();
}
