/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.List;
import java.util.Map;

import org.middleheaven.collections.KeyValuePair;

/**
 * 
 */
public class Enumerables {

	
	private Enumerables(){} 
	
	/**
	 * @return
	 */
	public static <T> Enumerable<T> emptyEnumerable() {
		return EmptyEnumerable.getInstance();
	}

	public static <T> Enumerable<T> asEnumerable(T element){
		return new SingleEnumerable<T>(element);
	}
	
	/**
	 * Converts an array of objects in an {@link Enumerable}.
	 * @param elements
	 * @return
	 */
	public static <T> Enumerable<T> asEnumerable(T ... elements){
		return new ArrayEnumerable<T>(elements);
	}

	/**
	 * Converts an Iterable of objects in an {@link Enumerable}.
	 * @param elements
	 * @return
	 */
	public static <T> Enumerable<T> asEnumerable(Iterable<? extends T> elements){
		return new IterableEnumerable<T>(elements);
	}
	
	/**
	 * Converts a List of objects in an {@link Enumerable}.
	 * @param elements
	 * @return
	 */
	public static <T> Enumerable<T> asEnumerable(List<? extends T> elements){
		return new ListEnumerable<T>(elements);
	}


	/**
	 * Converts an array of objects in an {@link Enumerable}.
	 * @param elements
	 * @return
	 */
	public static <K, V> Enumerable<KeyValuePair<K,V>> asEnumerable(Map<K,V> map){
		return new MapEnumerable<K,V>(map);
	}

	/**
	 * @param a
	 * @return
	 */
	public static <S, T extends S> Enumerable<S> safeCast(Enumerable<T> other) {
		return (Enumerable<S>) other;
	}

	/**
	 * @param i
	 * @param j
	 * @return
	 */
	public static Enumerable<Integer> range(int start, int end) {
		return new RangeEnumerable(start, end);
	}

}
