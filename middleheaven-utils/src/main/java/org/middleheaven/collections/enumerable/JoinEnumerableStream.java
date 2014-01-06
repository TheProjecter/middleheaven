/**
 * 
 */
package org.middleheaven.collections.enumerable;

import java.util.Iterator;

import org.middleheaven.collections.Pair;

/**
 * 
 */
class JoinEnumerableStream<T, P> extends AbstractEnumerable<Pair<T,P>> {

	
	private final Enumerable<T> left;
	private final Enumerable<P> right;
	
	public JoinEnumerableStream(Enumerable<T> left, Enumerable<P> right){
		this.left = left;
		this.right = right;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Pair<T, P>> iterator() {
		
		return new PairIterator<T,P>(left.iterator(), right.iterator());
		
	}
	
	private static class PairIterator<T,P> implements Iterator<Pair<T,P>>{

		
		private Iterator<T> left;
		private Iterator<P> right;

		public PairIterator(Iterator<T> left, Iterator<P> right){
			this.left = left;
			this.right = right;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return left.hasNext() && right.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Pair<T, P> next() {
			return new SimplePair<T,P>(left.next(), right.next());

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	private static class SimplePair<T,P> implements Pair<T,P>{
		
		private T left;
		private P right;

		public SimplePair(T left, P right){
			this.left = left;
			this.right = right;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public T left() {
			return left;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public P right() {
			return right;
		}
		
	}

}
