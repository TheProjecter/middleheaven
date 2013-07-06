package org.middleheaven.collections;

public interface Mergable<T> {

	public boolean canMergeWith(T other);
	public T merge (T other);
}
