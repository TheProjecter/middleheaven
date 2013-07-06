package org.middleheaven.collections;

import java.util.Map;

public interface PairEnumerable<K, V> extends Enumerable<Pair<K,V>> {

	
	public Map<K,V> into (Map<K,V> map);
}
