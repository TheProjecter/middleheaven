/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.KeyValuePair;
import org.middleheaven.collections.Pair;
import org.middleheaven.util.function.BinaryFunction;
import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
class EmptyEnumerable<T> implements Enumerable<T> , FastCountEnumerable{

	
	@SuppressWarnings("rawtypes")
	private static final EmptyEnumerable ME = new EmptyEnumerable();

	@SuppressWarnings("unchecked")
	public static <X> EmptyEnumerable<X> getInstance(){
		return ME;
	}
	
	private EmptyEnumerable(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return Collections.<T>emptySet().iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Block<T> walker) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<T>> L into(L collection) {
		return collection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getFirst() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> sort(Comparator<T> comparable) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> reject(Predicate<T> predicate) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(T object) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(Predicate<T> predicate) {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean every(Predicate<T> predicate) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean any(Predicate<T> predicate) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T find(Predicate<T> predicate) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> filter(Predicate<T> predicate) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> map(Function<C, T> Function) {
		return getInstance();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> map(BinaryFunction<C, T, Integer> Function) {
		return getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> mapAll(Function<Enumerable<C>, T> Function) {
		return getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T reduce(T seed, BinaryOperator<T> operator) {
		return seed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> C mapReduce(C seed, Function<Enumerable<C>, T> Function,
			BinaryOperator<C> operator) {
		return seed;
	}

	@Override
	public <K, V, P extends KeyValuePair<K, V>> PairEnumerable<K, V> pairMap(Function<KeyValuePair<K, V>, T> Function) {
		EmptyEnumerable<KeyValuePair<K, V>> instance = getInstance();
		return new PairEnumerableAdapter<K, V>(instance);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> PairEnumerable<C, Enumerable<T>> groupBy(Function<C, T> Function) {
		EmptyEnumerable<KeyValuePair<C,Enumerable<T>>> instance = getInstance();
		return new PairEnumerableAdapter<C, Enumerable<T>>(instance);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String join(String separator) {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> sort() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> Enumerable<U> cast(Class<U> newType) {
		return ME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getLast() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] asArray(T ... array) {
		return (T[])CollectionUtils.newArray(array.getClass().getComponentType(), 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> skip(int count) {
		return ME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T random() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T random(Random random) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> random(int count) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> random(int count, Random random) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> concat(Enumerable<? extends T> other) {
		return (Enumerable<T>) other;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <X extends T> Enumerable<T> concat(X element) {
		return (Enumerable<T>) Enumerables.asEnumerable(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> distinct() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> reverse() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <P> Enumerable<Pair<T, P>> join(Enumerable<P> other) {
		return getInstance();
	}


}
