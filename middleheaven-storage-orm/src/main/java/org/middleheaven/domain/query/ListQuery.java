package org.middleheaven.domain.query;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;


public class ListQuery<T> implements QueryResult<T> {
	
	private Callable<List<T>> callable;
	
	public ListQuery(Callable<List<T>> callable) {
		super();
		this.callable= callable;
	}
	
	public ListQuery(final List<T> other) {
		super();
		this.callable= new Callable<List<T>>() {

			@Override
			public List<T> call() throws Exception {
				return other;
			}
		};
	}

	private List<T> secureList(){
		List<T> list;
		try {
			list = this.callable.call();
			if (list == null){
				return Collections.emptyList();
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
	}
	
	@Override
	public long count() {
		return secureList().size();
	}

	@Override
	public T fetchFirst() {
		final List<T> list = secureList();
		if (list.isEmpty()){
			return null;
		}
		return list.get(0);
	}

	@Override
	public Collection<T> fetchAll() {
		return Collections.unmodifiableList(secureList());
	}

	@Override
	public boolean isEmpty() {
		return secureList().isEmpty();
	}

	@Override
	public QueryResult<T> limit(final int startAt, final int maxCount) {
		
		if (startAt < 1){
			throw new IllegalArgumentException("Range must start at position 1 or further");
		}
		
		return new ListQuery<T>(new Callable<List<T>>() {
			@Override
			public List<T> call() {
				final List<T> list = ListQuery.this.secureList();
				return list.subList(startAt-1, Math.min(list.size(),startAt+maxCount-1));
			}
			
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return secureList().iterator();
	}

}
