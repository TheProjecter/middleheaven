/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.KeyValuePair;
import org.middleheaven.collections.Pair;
import org.middleheaven.util.Joiner;
import org.middleheaven.util.function.BinaryFunction;
import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Function;
import org.middleheaven.util.function.NegatedPredicate;
import org.middleheaven.util.function.Predicate;

/**
 * 
 */
public abstract class AbstractEnumerable<T> implements Enumerable<T> {

	private static class ClassFunction<U,T> implements Function<U, T> {

		private Class<U> type;

		public ClassFunction(Class<U> newType){
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
	
	@SuppressWarnings("unchecked")
	protected Class<T> getGenericClass () {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	
	public int hashCode(){
		return 0;
	}
	
	public boolean equals(Object other ){
		if (this == other){
			return true;
		}
		return other instanceof Enumerable && equals((Enumerable<? extends T>)other);
	}

	public boolean equals(Enumerable<? extends T> other ){
		if (this == other){
			return true;
		}
		Iterator<T> a = this.iterator();
		Iterator<? extends T> b = other.iterator();
		
		if(a.hasNext() && b.hasNext()){
			do {
				
				if (!a.next().equals(b.next())){
					return false;
				}
			} while (a.hasNext() && b.hasNext());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<T> skip(int count) {
		return new SkipEnumerable<T>(count, this);
	}


	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> Enumerable<U> cast(final Class<U> newType) {
		return this.map(new ClassFunction<U,T>(newType));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final T[] asArray(T ... array) {

		if (isFastCount()){
			array = CollectionUtils.ensureExactLength(array, this.size());

			int index = 0;
		    for(T t : this){
		    	array[index++] = t;
		    }
		    
		    return array;
		} else {
			

			int index = 0;
		    for(T t : this){
		    	if (index > array.length -1){
		    		array = CollectionUtils.ensureMinLength(array, (array.length+1) * 2);
		    	}
		    	
		    	array[index++] = t;
		    }
		    
		    if (array.length > index )
		    {
		    	array = CollectionUtils.ensureExactLength(array,index );
		    }
		    
		    return array;
		}
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
	public void forEach(Block<T> block) {
		for (T t : this){
			block.apply(t);
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
	public <C> Enumerable<C> map(Function<C, T> Function) {
		return new TransformedEnumerableStream<C,T>(this, Function);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> map(BinaryFunction<C,T, Integer> Function){
		return new TransformedIndexedEnumerableStream<C,T>(this, Function);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> Enumerable<C> mapAll(Function<Enumerable<C>, T> Function) {
		return new FlattenEnumerableStream<C>(new TransformedEnumerableStream<Enumerable<C>,T>(this, Function));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <K, V, P extends KeyValuePair<K, V>> PairEnumerable<K, V> pairMap(Function<KeyValuePair<K, V>, T> Function) {
		return new PairEnumerableAdapter<K, V>(this.map(Function));
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <C> PairEnumerable<C, Enumerable<T>> groupBy(final Function<C, T> Function) {
		Map<C, Enumerable<T>> result = new HashMap<C,Enumerable<T>>();
		
		for (Iterator<T> it = iterator();it.hasNext();){
			T object = it.next();
			
			C key = Function.apply(object);
			Enumerable<T> items = result.get(key);
			
			if (items == null){
				items = Enumerables.emptyEnumerable();
			}
			
			items = items.concat(object);
			result.put(key,items);
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
	public <C> C mapReduce(C seed, Function<Enumerable<C>, T> Function, BinaryOperator<C> operator) {
		return new EnumerableStream<T>(this).mapAll(Function).reduce(seed, operator);
	}

	public T random(){
		return random(1).getFirst();
	}

	public T random(Random random){
		return random(1, random).getFirst();
	}
	

	public Enumerable<T> random(int count){
		return new RandomEnumerable<T>(count, this, new Random());
	}

	public Enumerable<T> random(int count,Random random){
		return new RandomEnumerable<T>(count, this, random);
	}
	
	public Enumerable<T> concat(Enumerable<? extends T> other){
		return ComposedEnumerable.compose(this,other);
	}
	
	public <X extends T> Enumerable<T> concat(X element){
		final Enumerable<? extends T> a = this;
		final Enumerable<? extends T> b = Enumerables.asEnumerable(element);
		return ComposedEnumerable.compose(a, b);
	}
	
	public Enumerable<T> distinct(){
		return new DistinctEnumerableStream<T>(this);
	}
	
	public Enumerable<T> reverse(){
		return new ReverseEnumerableStream<T>(this);
	}

	protected boolean isFastCount(){
		return this instanceof FastCountEnumerable;
	}
	
	public <P> Enumerable<Pair<T,P>> join(Enumerable<P> other){
		return new JoinEnumerableStream<T, P>(this, other);
	}

	
	
}
