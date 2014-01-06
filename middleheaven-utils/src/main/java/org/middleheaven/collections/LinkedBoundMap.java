package org.middleheaven.collections;

import java.util.LinkedHashMap;
import java.util.Map;

public final class LinkedBoundMap<K, V> extends AbstractBoundMap<K,V> {

	public LinkedBoundMap() {
		this(new LinkedHashMap<K,V>(), new LinkedHashMap<V,K>(),null);
	}

	private LinkedBoundMap(Map<K,V> direct, Map<V,K> reversed , BoundMap<V,K> inversed) {
		super(direct,reversed,inversed);
	}
	
	@Override
	protected BoundMap<V, K> createInverseMap(Map<V, K> reversed,Map<K, V> direct, BoundMap<K, V> inversed) {
		return new LinkedBoundMap<V,K>(reversed,direct,inversed);
	}


}
