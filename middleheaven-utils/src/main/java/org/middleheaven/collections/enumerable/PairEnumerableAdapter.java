package org.middleheaven.collections.enumerable;

import java.util.Iterator;
import java.util.Map;

import org.middleheaven.collections.KeyValuePair;

class PairEnumerableAdapter<K, V> extends AbstractEnumerable<KeyValuePair<K,V>> implements PairEnumerable<K, V> {
	
	
	private Enumerable<KeyValuePair<K, V>> original;

	public PairEnumerableAdapter(Enumerable<KeyValuePair<K,V>> original){
		this.original = original;
	}
	
	@Override
	public Iterator<KeyValuePair<K, V>> iterator() {
		return original.iterator();
	}

	@Override
	public Map<K, V> into(Map<K, V> map) {
		for (KeyValuePair<K,V> pair : this){
			map.put(pair.getKey(), pair.getValue());
		}
		return map;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isFastCount() {
		return original instanceof FastCountEnumerable;
	}

}
