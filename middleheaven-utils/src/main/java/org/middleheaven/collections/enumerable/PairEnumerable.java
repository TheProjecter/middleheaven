package org.middleheaven.collections.enumerable;

import java.util.Map;

import org.middleheaven.collections.KeyValuePair;

public interface PairEnumerable<K, V> extends Enumerable<KeyValuePair<K,V>> {

	
	public Map<K,V> into (Map<K,V> map);
}
