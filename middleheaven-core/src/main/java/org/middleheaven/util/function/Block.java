package org.middleheaven.util.function;

/**
 * Interface for the visitor pattern that can apply generic code based on an object.
 */
public interface Block<T> {

	public void apply(T value);
}
