package org.middleheaven.util.collections;

import java.util.Collection;


public interface TreeNode<T> extends Iterable<TreeNode<T>> , TreeWalkable<TreeNode<T>>{

	public TreeNode<T> getParent();
	public void setParent(TreeNode<T> parent);
	public T getValue();
	public Collection<TreeNode<T>> children();
	public boolean isEmpty();
	public int childCount();
	public void addChild(TreeNode<T> node);
	public void removeChild(TreeNode<T> node);
	
}