package org.middleheaven.collections;

public interface Mergeable<T> {

	public boolean canMergeWith(T other);
	public T merge (T other);
}
