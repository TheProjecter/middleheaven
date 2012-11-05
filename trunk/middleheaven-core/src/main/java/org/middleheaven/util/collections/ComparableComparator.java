package org.middleheaven.util.collections;

import java.util.Comparator;

/**
 * A {@link Comparator} than compares objects that implement {@link Comparable}.
 * @param <T> the type to compare.
 */
@SuppressWarnings("unchecked")
public class  ComparableComparator<T extends Comparable<? super T>> implements Comparator<T> {


	@SuppressWarnings("rawtypes")
	private static final ComparableComparator ME = new ComparableComparator();
	
	/**
	 * The instance of the {@link ComparableComparator}.
	 * @return the instance of the {@link ComparableComparator}.
	 */
	public static <X extends Comparable<? super X>> ComparableComparator<X> getInstance(){
		return ME;
	}
	
	private ComparableComparator(){}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int compare(T a, T b) {
		return ((Comparable)a).compareTo(b);
	}



}
