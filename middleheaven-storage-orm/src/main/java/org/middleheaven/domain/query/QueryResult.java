package org.middleheaven.domain.query;

import java.util.Collection;

public interface QueryResult<T> extends Iterable<T>{
	
	/**
	 * 
	 * @return the first element in the query range, or {@code null} if no elements exist. 
	 */
	public T fetchFirst();
	
	/**
	 * 
	 * @return a collection of elements in the query range
	 */
	public Collection<T> fetchAll();
	
	/**
	 * Return the total number of elements in the query.
	 * Even if the query was been paginated, {@code count} will allways return the
	 * total elements count number.
	 * @return the total number of elements in the query
	 */
	public long count();
	
	/**
	 * @return {@code true} if count would return 0, {@code false} otherwise.
	 */
	public boolean isEmpty();
	
	/**
	 * Creates a new query base on the same criteria used for {@code this} 
	 * but with limited elements 
	 * 
	 * @param startAt ordinal position of the first element in the new query (1 is the first)
	 * @param maxCount the maximum quantity of elements to fecth. 
	 * @return
	 */
	public QueryResult<T> limit(int startAt, int maxCount);
}
