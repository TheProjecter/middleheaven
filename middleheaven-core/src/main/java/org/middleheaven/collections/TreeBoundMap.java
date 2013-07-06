package org.middleheaven.collections;

import java.util.Map;
import java.util.TreeMap;

public final class TreeBoundMap<K, V> extends AbstractBoundMap<K,V> {

	public TreeBoundMap() {
		this(new TreeMap<K,V>(), new TreeMap<V,K>(),null);
	}

	private TreeBoundMap(Map<K,V> direct, Map<V,K> reversed , BoundMap<V,K> inversed) {
		super(direct,reversed,inversed);
	}
	
	@Override
	protected BoundMap<V, K> createInverseMap(Map<V, K> reversed,Map<K, V> direct, BoundMap<K, V> inversed) {
		return new TreeBoundMap<V,K>(reversed,direct,inversed);
	}


}
