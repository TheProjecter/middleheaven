package org.middleheaven.collections;

import java.util.Collection;
import java.util.Comparator;

import org.middleheaven.util.function.BinaryOperator;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.function.Predicate;

/**
 * Represents an sequence of objects that's still iterable and permits to query for the sequence size.
 * In addition provides several methods for manipulating the data in the collection. 
 *
 * @param <T>
 */
public interface Enumerable<T> extends Iterable<T>{
	
	/**
	 * Executes a {@link Block} of code for each instance of <T> in this.
	 * @param block the block to invoke.
	 */
	public void forEach(Block<T> block);
	
	/**
	 * Adds the contents of the enumerable into the given {@link Collection}.
	 * @param collection the given collection.
	 * @return the given collection
	 */
	public <L extends Collection<T>> L into(L collection);
	
	/**
	 * Adds the contents of the enumerable into the given array.
	 * If the arrays as the proper lenght, its is populated and returned. Otherwise a new array is created with the proper length and returned.
	 * @param collection the given collection.
	 * @return the given collection
	 */
	public T[] intoArray(T[] array);
	
	/**
	 * 
	 * @param newType
	 * @return
	 */
	public <U> Enumerable<U> cast(Class<U> newType); 
	
	/**
	 * 
	 * @return the number of elements in {@code this}.
	 */
	public int size();
	
	/**
	 * 
	 * @return <code>true</code> if the size is zero, <code>false</code> otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * @return the fist element in the {@link Enumerable} or <code>null</code> if the {@link Enumerable} is empty.
	 */
	public T getFirst();
	
	/**
	 * @return the last element in the {@link Enumerable} or <code>null</code> if the {@link Enumerable} is empty.
	 */
	public T getLast();
	
	/**
	 * Returns an enumerable in the order atributed by the given comparator.
	 * @param comparable
	 * @return
	 */
	public Enumerable<T> sort (Comparator<T> comparable);
	
	/**
	 * Returns an enumerable in the order atributed by the given comparator.
	 * @param comparable
	 * @return
	 * @throws IllegalArgumentException if T is not comparable
	 */
	public Enumerable<T> sort ();
	
	/**
	 * 
	 * Produces an {@link Enumerable} of elements of {@code this} that are not rejected
	 * by the given predicate.
	 * 
	 * @param predicate the rejection predicate. if the predicate returns <code>true</code> the element is reject and is not added to the result collection
	 * @return an {@link Enumerable} with the not rejected elements
	 */
	public Enumerable<T> reject(Predicate<T> predicate); 
	
	/**
	 * Counts the number of copies of object in the collection.
	 * This method trust the implementation of equals and hashcode in the object.
	 * 
	 * @param object the object to test
	 * @return the number of copies.
	 */
	public int count(T object);
	
	public int count(Predicate<T> predicate);
	
	/**
	 * Returns <code>true</code> if all items match the predicate.
	 * 
	 * @param predicate the classifier with the matching rule.
	 * @return <code>true</code> if all items match the predicate, <code>false</code> otherwise.
	 */
	public boolean every(Predicate<T> predicate);
	
	/**
	 * Returns true if any item matchs the predicate.
	 * 
	 * @param predicate the predicate with the matching rule.
	 * @return <code>true</code> if any item matchs the predicate, <code>false</code> otherwise.
	 */
	public boolean any(Predicate<T> predicate);
	
	/**
	 * Finds first item matching the classifier.
	 * 
	 * @param predicate the classifier with the matching rule.
	 * @return first item matching the classifier
	 */
	public T find(Predicate<T> predicate);
	
	/**
	 * finds all items matching the classifier
	 * @param predicate the classifier with the matching rule.
	 * @return finds all items matching the classifier
	 */
	public Enumerable<T> filter(Predicate<T> predicate);
	
	/**
	 * Collect the return value of calling a classifier on each item in a collection into a new collection.
	 * If the classifier returns {@code null} for a value, it will not be added to the result collection.
	 * 
	 * @param <C> the result type.
	 * @param classifier classifier the classifier with the matching rule.
	 * @return the collect itens.
	 */
	public <C> Enumerable<C> map(Mapper<C,T> mapper);
	
	public <K, V, P extends Pair<K,V>> PairEnumerable<K,V> pairMap(Mapper<Pair<K,V>,T> mapper);
	
	public <C> Enumerable<C> mapAll(Mapper<Enumerable<C>,T> mapper);
	
	public T reduce(T seed, BinaryOperator<T> operator);
	
	public <C> C mapReduce(C seed, Mapper<Enumerable<C>,T> mapper, BinaryOperator<C> operator);
	
	/**
	 * Groups elements by classification C.
	 * 
	 * @param <C> the type of classification
	 * @param mapper   
     * @return a {@link Enumerable} of {@link Pair} of keys and values
	 */
	public <C> PairEnumerable<C, Enumerable<T>> groupBy(Mapper<C,T> mapper);

	/**
	 * Converters all elements to a string separated by a given separator. 
	 * Elements are converted invoking {@code toString()}.
	 * 
	 * @param separator separator used between elements
	 * @return a string of the String representation of the elements.
	 */
	public String join(String separator);


	
}
