package org.middleheaven.util.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

/**
 * Several utilitary methods 
 */
public class CollectionUtils {

	/**
	 * Merges two arrays of {@link Mergable} objects.
	 * @param a first array
	 * @param b secound array
	 * @return an array with all merges done.
	 */
	public static <T extends Mergable<T>> T[] merge(T[] a, T[] b){
		if (a == null || a.length ==0){
			return b;
		} else if (b == null || b.length ==0){
			return a;
		}
		
		Object newArray = Array.newInstance(a[0].getClass(), a.length+b.length);
		
		int position =0; 
		outter: for (T i : a){
			for (T j : b){
				if (i.canMergeWith(j)){
					Array.set(newArray, position, i.merge(j));
					position++;
					continue outter;
				}
			}
		}
		
		if (position == 0){
			return (T[]) Array.newInstance(a[0].getClass(),0);
		} else {
			Object result = Array.newInstance(a[0].getClass(),position);
			
			System.arraycopy(newArray, 0, result, 0, position);
			return (T[])result;
		}
		
	}
	
	/**
	 * Merges two lists of {@link Mergable} objects.
	 * @param a first list
	 * @param b secound list
	 * @return a list with all merges done.
	 */
	public static <T extends Mergable<T>> List<T> merge(List<T> a, List< T> b){
		if (a.isEmpty()){
			return b;
		} else if (b.isEmpty()){
			return a;
		}
		

		
		List<T> result = new LinkedList<T>();
		outter: for (T i : a){
			for (T j : b){
				if (i.canMergeWith(j)){
					result.add(i.merge(j));
					continue outter;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Converts an array of objects in an {@link EnhancedList}.
	 * @param elements
	 * @return
	 */
	public static <T> Enumerable<T> asEnumerable(T ... elements){
		return new ArrayEnumerable<T>(elements);
	}
	
	/**
	 * Converts an array of objects in an {@link EnhancedList}.
	 * @param elements
	 * @return
	 */
	public static <T> Enumerable<T> asEnumerable(Iterable<T> elements){
		return new IterableEnumerable<T>(elements);
	}
	
	
	/**
	 * Converts an array of objects in an {@link EnhancedList}.
	 * @param elements
	 * @return
	 */
	public static <K, V> Enumerable<Pair<K,V>> asEnumerable(Map<K,V> map){
		return new MapEnumerable<K,V>(map);
	}
	
	
	/**
	 * Ensures the given is sortable, i.e. is a List and implements {@link RandomAccess}. If not
	 * it will be copied to a collection that is sortable.
	 * @param collection
	 * @return
	 */
	public static <T> List<T> ensureSortable(Collection<T> collection){
		
		if (collection == null){
			return null;
		}
		
		if(collection instanceof RandomAccess && collection instanceof List && !collection.getClass().getName().toLowerCase().contains("unmodifiable")){
			return (List<T>)collection;
		}
		
		return new ArrayList<T>(collection);
	}
	
	
	public static <T> Iterator <T> singleIterator(T object){
		return new SingleIterator<T>(object);
	}

	public static <T> Iterator <T> arrayIterator(T[] array){
		return new ArrayIterator<T>(array);
	}

	private static class SingleIterator<T> implements Iterator<T>{

		T object;
		boolean iterated = false;
		public SingleIterator(T object) {
			this.object = object;
		}

		@Override
		public boolean hasNext() {
			return !iterated;
		}

		@Override
		public T next() {
			iterated =true;
			return object;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/**
	 * Collections are equals if they contain the same elements 
	 * independent of ordering.
	 * To this to be true, they must have the same number of elements.
	 * Empty collections are equal.
	 * 
	 * @param <T>
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static <T> boolean equalContents(Collection<? extends T> c1,Collection<? extends T> c2) {

		if (c1==c2){
			return true;
		} else if (c1.isEmpty() && c2.isEmpty()){
			return true;
		} if (c1.size()!=c2.size()){
			return false;
		}
	
		
		if (!(c2 instanceof Set) && (c1 instanceof Set || c1.size() > c2.size())) {
			//swap
			Collection<? extends T> tmp = c1;
			c1 = c2;
			c2 = tmp;
		}

		for (T t : c1){
			if (!c2.contains(t)){
				return false;
			}
		}
		return true;

	}

	/**
	 * Determine if the contents of two maps are the same, i.e. the same keys are mapped to the same values in both maps.
	 * @param a the first {@link Map}
	 * @param b the secound {@link Map}
	 * @return <code>true</code> if the contents are the same, <code>false</code> otherwise.
	 */
	public static <K,V> boolean equalContents(Map<? extends K, ? extends V> a,Map<? extends K, ? extends V> b) {
		if (a == b){
			return true;
		} else if (a.isEmpty() && b.isEmpty()){
			return true;
		} if (a.size()!=b.size()){
			return false;
		} else {
	
			
			for (Map.Entry entryA : a.entrySet()){
				
				if (!b.containsKey(entryA.getKey()) || !b.get(entryA.getKey()).equals(entryA.getValue())){
					return false;
				}
				
			}
				
			
			return true;
		}

	}

	/**
	 * Returns a collection with the common elements in c1 and c2
	 * @param <T>
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static <T> Collection<T> intersect(Collection<T> c1,Collection<T> c2) {

		if (c1.isEmpty() || c2.isEmpty()){
			return Collections.emptySet();
		}
		
		if (!(c2 instanceof Set) && (c1 instanceof Set || c1.size() > c2.size())) {
			//swap
			Collection<T> tmp = c1;
			c1 = c2;
			c2 = tmp;
		}

		Collection<T> result = new HashSet<T>();
		for (T obj : c1){
			if (c2.contains(obj)){
				result.add(obj);
			}
		}
		
		return result;
	}


	@SuppressWarnings("unchecked")
	public static <T> T[] addToArray(T[] array1,T[] array2) {
		
		Class<?> componentType = array1.getClass().getComponentType();
		
		Object newArray = Array.newInstance(componentType, array1.length+array2.length);
		
		System.arraycopy(array1, 0, newArray, 0, array1.length);
		System.arraycopy(array2, 0, newArray, array1.length,array2.length);
		
		return (T[]) newArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] appendToArrayEnd(T[] array,T element , T ... elements) {
		
		Class<?> componentType = array.getClass().getComponentType();
		
		Object newArray = Array.newInstance(componentType, array.length+1+elements.length);
		
		System.arraycopy(array, 0, newArray, 0, array.length);
		Array.set(newArray, array.length , element);
		System.arraycopy(elements, 0, newArray, array.length +1,elements.length);
		
		return (T[]) newArray;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] appendToArrayBegining(T[] array,T element , T ... elements) {
		
		Class<?> componentType = array.getClass().getComponentType();
		
		Object newArray = Array.newInstance(componentType, array.length+1+elements.length);
		
	
		Array.set(newArray, 0 , element);
		if (elements.length > 0){
			System.arraycopy(elements, 0, newArray, 1,elements.length);
			System.arraycopy(array, 0, newArray, elements.length, array.length);
		} else {
			System.arraycopy(array, 0, newArray, 1, array.length);
			
		}
		
		return (T[]) newArray;
	}
	
	


	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<T> arrayType, int length) {
		
		Object newArray = Array.newInstance(arrayType, length);
		return (T[]) newArray;
	}

	public static <T> boolean arrayContains(T[] array, T candidate ) {
		for (T t : array){
			if (t == candidate || (t != null && t.equals(candidate))){
				return true;
			}
		}
		return false;
	}

	/**
	 * @param enumeration
	 * @return
	 */
	public static <T> Iterator<T> enumationIterator(Enumeration<T> enumeration) {
		return new EnumerationIterator<T>(enumeration);
	}

	/**
	 * @param values
	 * @param class1
	 * @return
	 */
	public static <T> Collection<T> secureCoerce(Collection<? extends T> values,Class<T> class1) {
		return (Collection<T>) values;
	}

	/**
	 * @param values
	 * @param class1
	 * @return
	 */
	public static <T> Iterator<T> secureCoerce(Iterator<? extends T> values,Class<T> class1) {
		return (Iterator<T>) values;
	}

	/**
	 * Ensure the result implements {@link RandomAccess}.
	 * @param collection the original collections
	 * @return a collection that implements {@link RandomAccess}.
	 */
	public static <T> List<T> ensureRandomAcess(List<T> collection) {
		if (collection instanceof RandomAccess){
			return collection;
		} else {
			return new ArrayList<T>(collection);
		}
		
	}

	/**
	 * @return
	 */
	public static <T> Enumerable<T> emptyEnumerable() {
		return EmptyEnumerable.getInstance();
	}


}
