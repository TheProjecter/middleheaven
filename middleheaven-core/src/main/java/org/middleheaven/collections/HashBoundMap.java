package org.middleheaven.collections;

import java.util.HashMap;
import java.util.Map;

public final class HashBoundMap<K, V> extends AbstractBoundMap<K,V>{

	public HashBoundMap() {
		this(new HashMap<K,V>(), new HashMap<V,K>(),null);
	}

	private HashBoundMap(Map<K,V> direct, Map<V,K> reversed , BoundMap<V,K> inversed) {
		super(direct,reversed,inversed);
	}
	
	@Override
	protected BoundMap<V, K> createInverseMap(Map<V, K> reversed,Map<K, V> direct, BoundMap<K, V> inversed) {
		return new HashBoundMap<V,K>(reversed,direct,inversed);
	}

}
