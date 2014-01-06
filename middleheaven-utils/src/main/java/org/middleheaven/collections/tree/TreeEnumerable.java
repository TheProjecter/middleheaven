package org.middleheaven.collections.tree;

import org.middleheaven.util.function.Block;

/**
 * Allows for iteration in a tree like structure
 * @param <T>
 */
public interface TreeEnumerable<T>  {

	/**
	 * Executes a block of code for each child node and its children, recursvely downward 
	 * @param block a block of code to execute.
	 */
	public void forEachRecursive(Block<T> block);
	
	
	/**
	 * Executes a block of code for each parente children node, recursvely upward
	 * @param block a block of code to execute.
	 */
	public void forEachParent(Block<T> block);
}
