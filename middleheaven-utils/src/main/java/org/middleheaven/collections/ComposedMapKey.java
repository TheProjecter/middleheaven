package org.middleheaven.collections;

/**
 * An object composed by other objects
 * that is intended as key in a {@code Map} object.
 */
public abstract class ComposedMapKey {

	public abstract boolean equals(Object other);
	public abstract int hashCode();
}
