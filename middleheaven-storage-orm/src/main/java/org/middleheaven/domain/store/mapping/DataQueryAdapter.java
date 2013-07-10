/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.domain.query.QueryResult;
import org.middleheaven.persistance.DataQuery;
import org.middleheaven.persistance.DataRowStream;

/**
 * @param <T> the type of the  resulting entity.
 */
public class DataQueryAdapter<T> implements QueryResult<T> {

	
	private DataQuery query;
	private EntityModelDataSetMapping mapping;

	/**
	 * 
	 * Constructor.
	 * @param mapping a {@link EntityModelDataSetMapping} to use for this adapter
	 * @param query a {@link DataQuery} to be adapted to a {@link QueryResult} object.
	 */
	public DataQueryAdapter(EntityModelDataSetMapping mapping, DataQuery query) {
		this.query = query;
		this.mapping = mapping;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T fetchFirst() {
		DataQuery q = query.limit(1, 1);
		
		DataRowStream stream = q.getRowStream();
		try {
			if (stream.next()){
				@SuppressWarnings("unchecked")
				final T t =  (T) mapping.read(stream.currentRow());
				return t;
			} else {
				return null;
			}
		} finally {
			stream.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<T> fetchAll() {
		DataRowStream stream = query.getRowStream();
		try {
			List<T> result = new LinkedList<T>();
		
			while (stream.next()) {
				@SuppressWarnings("unchecked")
				final T t = (T) mapping.read(stream.currentRow());
				result.add(t);
			}
			return result;
		} finally {
			stream.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long count() {
		return query.rowCount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return count() == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QueryResult<T> limit(int startAt, int maxCount) {
		return new DataQueryAdapter<T>(mapping, query.limit(startAt, maxCount));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		
		return new RowIteratorAdapter<T>(query.getRowStream());
	}
	
	private class RowIteratorAdapter<X> implements Iterator<X> {

		private DataRowStream stream;

		public RowIteratorAdapter(DataRowStream stream){
			this.stream = stream;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			boolean hasNext = stream.next();
			if (!hasNext){
				stream.close();
			} 
			return hasNext;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public X next() {
			return (X) mapping.read(stream.currentRow());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("Not supportted operation");
		}
		
	}

}
