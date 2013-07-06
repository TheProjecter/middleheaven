package org.middleheaven.collections;

import java.util.Iterator;
import java.util.Map;

public class PairEnumerableAdapter<K, V> extends AbstractEnumerable<Pair<K,V>> implements PairEnumerable<K, V> {
	
	
	private Enumerable<Pair<K, V>> original;

	public PairEnumerableAdapter(Enumerable<Pair<K,V>> original){
		this.original = original;
	}
	
	@Override
	public Iterator<Pair<K, V>> iterator() {
		return original.iterator();
	}

	@Override
	public Map<K, V> into(Map<K, V> map) {
		for (Pair<K,V> pair : this){
			map.put(pair.getKey(), pair.getValue());
		}
		return map;
	}

}
