package org.middleheaven.domain.query;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * 
 * @param <T>
 */
public final class EmptyQuery<T> implements QueryResult<T>{

	@Override
	public long count() {
		return 0;
	}

	@Override
	public T fetchFirst() {
		return null;
	}

	@Override
	public Collection<T> fetchAll() {
		return Collections.emptyList();
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public QueryResult<T> limit(int startAt, int maxCount) {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return Collections.<T>emptyList().iterator();
	}

}
