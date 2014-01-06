package org.middleheaven.collections.tree;

import java.util.Collection;

import org.middleheaven.collections.enumerable.Enumerable;



public interface TreeNode<T> extends TreeEnumerable<TreeNode<T>> , Enumerable<TreeNode<T>>{

	public TreeNode<T> getParent();
	public void setParent(TreeNode<T> parent);
	public T getValue();
	public Collection<TreeNode<T>> children();
	public boolean isEmpty();
	public int childCount();
	public void addChild(TreeNode<T> node);
	public void removeChild(TreeNode<T> node);
	
}
