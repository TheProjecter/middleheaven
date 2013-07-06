/**
 * 
 */
package org.middleheaven.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.middleheaven.util.Joiner;
import org.middleheaven.util.classification.NegatedPredicate;
import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public abstract class AbstractEnumerable<T> implements Enumerable<T> {

	private static class ClassMapper<U,T> implements Mapper<U, T> {

		private Class<U> type;

		public ClassMapper(Class<U> newType){
			this.type = newType;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public U apply(T obj) {
			return type.cast(obj);
		}
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> Enumerable<U> cast(final Class<U> newType) {
		return this.map(new ClassMapper<U,T>(newType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T[] intoArray(T[] array) {

		array = CollectionUtils.ensureExactLength(array, size());
		
		int i =0;
		for (T t : this){
			array[i] = t;
			i++;
		}
		
		return array;
	}
	
	// final 
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty(){
		return !this.iterator().hasNext();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		int count = 0;
		for (Iterator<T> it = this.iterator(); it.hasNext(); it.next()){
			count++;
		}
		return count;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Enumerable<T> sort(Comparator<T> comparable) {
		
		ArrayList<T> all = this.into(new ArrayList<T>());
		
		Collections.sort(all, comparable);
		
		return new IterableEnumerable<T>(all);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Enumerable<T> sort() {
		ArrayList all = this.into(new ArrayList());
		
		if (!all.isEmpty() && !Comparable.class.isInstance(all.get(0))){
			throw new IllegalArgumentException("Element is not Comparable");
		}
		Collections.sort(all);
		
		return new IterableEnumerable<T>(all);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String join(String separator) {
		return Joiner.with(separator).join(this);

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(final T object) {
		if (object == null){
			return 0;
		}
		return count(new Predicate<T>() {

			@Override
			public Boolean apply(T obj) {
				return object.equals(obj);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int count(Predicate<T> predicate){
		int count =0; 
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = predicate.apply(o);
			if (b!=null && !b.booleanValue()){
				count++;
			}
		}
		return count;
	}
	


	@Override
	public final Enumerable<T> reject(Predicate<T> predicate){
		return filter(new NegatedPredicate<T>(predicate));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean every(Predicate<T> predicate) {
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = predicate.apply(o);
			if (b!=null && !b.booleanValue()){
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean any(Predicate<T> predicate) {
		for (Iterator<T> it = iterator();it.hasNext();){
			T o = it.next();
			Boolean b = predicate.apply(o);
			if (b!=null && b.booleanValue()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Block<T> walker) {
		for (T t : this){
			walker.apply(t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<T>> L into(L collection) {
		for (T t : this){
			collection.add(t);
		}
		return collection;
	}


	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getFirst() {
		Iterator<T> it = this.iterator();
		return it.hasNext() ? it.next() : null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getLast() {
		Iterator<T> it = this.iterator();
		T last = null;
		while(it.hasNext()){
			last = it.next();
		}
		return last;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public T find(Predicate<T> predicate) {
		return filter(predicate).getFirst();
	}
	
	
	// stream 
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Enumerable<T> filter(Predicate<T> predicate) {
		return new FilteredEnumerableStream<T>(this, predicate);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> map(Mapper<C, T> mapper) {
		return new TransformedEnumerableStream<C,T>(this, mapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> mapAll(Mapper<Enumerable<C>, T> mapper) {
		return new FlattenEnumerableStream<C>(new TransformedEnumerableStream<Enumerable<C>,T>(this, mapper));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V, P extends Pair<K, V>> PairEnumerable<K, V> pairMap(Mapper<Pair<K, V>, T> mapper) {
		return new PairEnumerableAdapter<K, V>(this.map(mapper));
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> PairEnumerable<C, Enumerable<T>> groupBy(final Mapper<C, T> mapper) {
		Map<C, EnhancedArrayList<T>> result = new HashMap<C,EnhancedArrayList<T>>();
		for (Iterator<T> it = iterator();it.hasNext();){
			T object = it.next();
			
			EnhancedArrayList<T> items = result.get(mapper.apply(object));
			
			if (items == null){
				items = new EnhancedArrayList<T>();
			}
			
			items.add(object);
		}
		
		return new MapPairEnumerable<C, Enumerable<T>> (result);

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T reduce(T seed , BinaryOperator<T> operator) {

		T result = seed;
		for (T t: this){
			result = operator.apply(result, t);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> C mapReduce(C seed, Mapper<Enumerable<C>, T> mapper, BinaryOperator<C> operator) {
		return new EnumerableStream<T>(this).mapAll(mapper).reduce(seed, operator);
	}

	
}
