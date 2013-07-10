/**
 * 
 */
package org.middleheaven.domain.query;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * 
 */
public class SingleQueryResult<T> implements QueryResult<T> {

	private T obj;

	public SingleQueryResult(T obj){
		this.obj  = obj;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T fetchFirst() {
		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<T> fetchAll() {
		return obj == null ? Collections.<T>emptySet() : Collections.singleton(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long count() {
		return obj == null ? 0L : 1L;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		 return obj == null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryResult<T> limit(int startAt, int maxCount) {
		return this;
	}

}
