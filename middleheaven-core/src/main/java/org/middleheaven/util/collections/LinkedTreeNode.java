package org.middleheaven.util.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;


public class LinkedTreeNode<E> implements TreeNode<E>{

	private List<TreeNode<E>> nodes = new LinkedList<TreeNode<E>>();
	E value;
	private TreeNode<E> parent;
	
	public void addChild(TreeNode<E> node){
		nodes.add(node);
	}
	
	public void removeChild(TreeNode<E> node){
		nodes.remove(node);
	}
	
	public Iterator<TreeNode<E>> iterator(){
		return nodes.iterator();
	}
	
	public E getValue(){
		return value;
	}
	
	public void setValue(E value){
		this.value = value;
	}

	@Override
	public int childCount() {
		return nodes.size();
	}

	@Override
	public Collection<TreeNode<E>> children() {
		return nodes;
	}

	@Override
	public TreeNode<E> getParent() {
		return this.parent;
	}

	@Override
	public boolean isEmpty() {
		return this.nodes.isEmpty();
	}

	@Override
	public void setParent(TreeNode<E> parent) {
		if(this.parent !=null && !this.parent.equals(parent)){
			throw new IllegalStateException("Parent already set");
		}
		this.parent = parent;
	}

	@Override
	public void forEachParent(Block<TreeNode<E>> walker) {
		if (parent != null){
			walker.apply(this.parent);
			parent.forEachParent(walker);
		}
	}

	@Override
	public void forEachRecursive(Block<TreeNode<E>> block) {
		for (TreeNode<E> t : this){
			block.apply(t);
			t.forEachRecursive(block);
		}
	}

	@Override
	public void forEach(Block<TreeNode<E>> walker) {
		for (TreeNode<E> t : this){
			walker.apply(t);
		}
	}
	


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<TreeNode<E>>> L into(L collection) {
		for (TreeNode<E> t : this){
			collection.add(t);
		}
		
		return collection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<TreeNode<E>> filter(Predicate<TreeNode<E>> predicate) {
		return new IterableEnumerable<TreeNode<E>>(this).filter(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> map(Mapper<C, TreeNode<E>> classifier) {
		return new IterableEnumerable<TreeNode<E>>(this).map(classifier);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> Enumerable<U> cast(Class<U> newType) {
		return new IterableEnumerable<TreeNode<E>>(this).cast(newType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.nodes.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeNode<E> getFirst() {
		return this.nodes.get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<TreeNode<E>> sort(Comparator<TreeNode<E>> comparable) {
		return new IterableEnumerable<TreeNode<E>>(this).sort(comparable);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<TreeNode<E>> sort() {
		return new IterableEnumerable<TreeNode<E>>(this).sort();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<TreeNode<E>> reject(Predicate<TreeNode<E>> predicate) {
		return new IterableEnumerable<TreeNode<E>>(this).reject(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(TreeNode<E> object) {
		return new IterableEnumerable<TreeNode<E>>(this).count(object);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(Predicate<TreeNode<E>> predicate) {
		return new IterableEnumerable<TreeNode<E>>(this).count(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean every(Predicate<TreeNode<E>> predicate) {
		return new IterableEnumerable<TreeNode<E>>(this).every(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean any(Predicate<TreeNode<E>> predicate) {
		return new IterableEnumerable<TreeNode<E>>(this).any(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeNode<E> find(Predicate<TreeNode<E>> predicate) {
		return new IterableEnumerable<TreeNode<E>>(this).find(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> mapAll(Mapper<Enumerable<C>, TreeNode<E>> mapper) {
		return new IterableEnumerable<TreeNode<E>>(this).mapAll(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeNode<E> reduce(TreeNode<E> seed,
			BinaryOperator<TreeNode<E>> operator) {
		return new IterableEnumerable<TreeNode<E>>(this).reduce(seed, operator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> C mapReduce(C seed, Mapper<Enumerable<C>, TreeNode<E>> mapper,
			BinaryOperator<C> operator) {
		return new IterableEnumerable<TreeNode<E>>(this).mapReduce(seed, mapper, operator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> PairEnumerable<C, Enumerable<TreeNode<E>>> groupBy(Mapper<C, TreeNode<E>> mapper) {
		return new IterableEnumerable<TreeNode<E>>(this).groupBy(mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String join(String separator) {
		return new IterableEnumerable<TreeNode<E>>(this).join(separator);
	}

	@Override
	public <K, V, P extends Pair<K, V>> PairEnumerable<K, V> pairMap(Mapper<Pair<K, V>, TreeNode<E>> mapper) {
		return new IterableEnumerable<TreeNode<E>>(this).pairMap(mapper);
	}

	

}
