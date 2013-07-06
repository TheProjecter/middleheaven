/**
 * 
 */
package org.middleheaven.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * 
 */
public final class LinkedSparseList<T> implements SparseList<T> {

	private Entry<T> first;
	private Entry<T> last;
	private int maxIndex = -1;
	private T emptyValue;

	public LinkedSparseList(T emptyValue){
		first = new Entry<T>(-1, emptyValue);
		last = first;
		this.emptyValue  = emptyValue;
	}

	public LinkedSparseList (T emptyValue, Collection<? extends T> other){
		this(emptyValue);
		addAll(other);
	}
	
	public LinkedSparseList (T emptyValue, int size){
		this(emptyValue);
		maxIndex = size-1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(T obj) {
		Entry<T> newEntry = new Entry<T>(++maxIndex,obj);
		last.next = newEntry;
		newEntry.previous = last;
		last = newEntry;
		return true;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends T> other) {
		for(T obj : other){
			last.next = new Entry<T>(++maxIndex, obj);
			last = last.next;
		}
		return true;
	}

	private Entry<T> FindEntryByIndex(int index){
		Entry<T> entry = first;
		while (entry.next != null){
			entry = entry.next;
			if (entry.index == index){
				return entry;
			}
		}
		return null;
	}
	
	private Entry<T> FindEntryByObject(Object obj){
		Entry<T> entry = first;
		while (entry.next != null){
			entry = entry.next;
			if (entry.object.equals(obj)){
				return entry;
			}
		}
		return null;
	}
	
	private Entry<T> FindNearestByIndex(int index){
		Entry<T> near = first;
		Entry<T> entry = first;
		while (entry.next != null){
			entry = entry.next;
			if (entry.index < index){
				near = entry;
			}
			else if (entry.index == index){
				return entry;
			}
			else {
				return near;
			}
		}
		return entry;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public T remove(int index) {
		Entry<T> entry = FindEntryByIndex(index);
		
		if (entry == null){
			return emptyValue;
		} else {
		
			Entry<T> previous = entry.previous;
			Entry<T> next = entry.next;
			
			next.previous = previous;
			previous.next = next;
			
			return entry.object;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object obj) {
	    Entry<T> entry = FindEntryByObject(obj);
		
		if (entry == null){
			return false;
		} else {
		
			Entry<T> previous = entry.previous;
			Entry<T> next = entry.next;
			
			next.previous = previous;
			previous.next = next;
			
			return true;
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(Object obj) {
		Entry<T> entry = last;
		while (entry.previous != null){
			entry = entry.previous;
			if (entry.object.equals(obj)){
				return entry.index;
			}
		}
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(int index, Collection<? extends T> all) {
		Entry<T> nearest = FindNearestByIndex(index);
		Entry<T> current = nearest;
		if ( nearest.index == index){
			shiftForwardFrom(nearest);
			current = nearest.previous;
		}
		
		int newIndex = index;
		for (T obj : all){
			Entry<T> newEntry = new Entry<T>(newIndex,obj);
			current.next = newEntry;
			newEntry.previous = current;
			current = newEntry;
			return true;
		}
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int index, T obj) {
		Entry<T> nearest = FindNearestByIndex(index);
		if ( nearest.index == index){
			// exactly
			Entry<T> newEntry = new Entry<T>(index, obj);
			Entry<T> before = nearest.previous;
			newEntry.previous = before;
			before.next = newEntry;
			nearest.previous = newEntry;
			newEntry.next = nearest;
			 // shift all others
			shiftForwardFrom(nearest);
		} else {
			// nearest
			Entry<T> next = nearest.next;
			Entry<T> newEntry = new Entry<T>(index, obj);
			nearest.next = newEntry;
			newEntry.next = next;
			newEntry.previous = nearest;
		}
	}

	private void shiftForwardFrom(Entry<T> entry) {
		if (entry != null){
			entry.index++;
			Entry<T> a = entry;
			while (a.next != null){
				a = a.next;
				a.index++;
			}
			maxIndex = a.index;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(int index) {
		checkBounds(index);
		Entry<T> entry = FindEntryByIndex(index);
		
		if (entry == null){
			return emptyValue;
		} else {
			return entry.object;
		}
	}

	/**
	 * @param index
	 */
	private void checkBounds(int index) {
	   if (index <0 || index > maxIndex){
		   throw new ArrayIndexOutOfBoundsException(index);
	   }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(Object obj) {
		Entry<T> entry = first;
		while (entry.next != null){
			entry = entry.next;
			if (entry.object.equals(obj)){
				return entry.index;
			}
		}
		return -1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		first = new Entry<T>(-1, null);
		last = first;
		maxIndex = -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object obj) {
		Entry<T> entry = first;
		while (entry.next != null){
			entry = entry.next;
			if (entry.object.equals(obj)){
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> all) {
		for (Object obj : all){
			if (!this.contains(obj)){
				return false;
			}
		}
		return true;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return first.next == null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new SparseIterator <T>(first);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<T> listIterator() {
		return listIterator(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<T> listIterator(int index) {
		Entry<T> it = FindEntryByIndex(index);
		return new SparseListIterator <T>(it);
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> all) {
		for (Iterator<T> it = this.iterator(); it.hasNext(); ){
			if (all.contains(it.next())){
				it.remove();
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> all) {
		for (Iterator<T> it = this.iterator(); it.hasNext(); ){
			if (!all.contains(it.next())){
				it.remove();
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T set(int index, T obj) {
		
		Entry<T> entry =  FindNearestByIndex(index);
		T other = null;
		if (entry.index == index){
			other = entry.object;
			entry.object = obj;
			maxIndex = Math.max(maxIndex, index);
		} else if (entry == last){
			Entry<T> newEntry = new Entry<T>(index,obj);
			last.next = newEntry;
			newEntry.previous = last;
			last = newEntry;
			maxIndex = Math.max(maxIndex, index);
		} else {
			add(index, obj);
		}
		return other;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return maxIndex + 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> subList(int startIndex, int endIndex) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return toArray(new Object[maxIndex]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <U> U[] toArray(U[] array) {
		Arrays.fill(array, emptyValue);
		Entry<T> entry = this.first;
		while (entry.next != null){
			entry = entry.next;
			array[entry.index] = (U)entry.object;
		}
		return array;
	}


	private static class Entry<T> {

		/**
		 * Constructor.
		 * @param i
		 * @param obj
		 */
		public Entry(int i, T obj) {
			this.index = i;
			this.object = obj;
		}
		public int index;
		public T object;
		public Entry<T> previous;
		public Entry<T> next;
	}
	
	private static class SparseIterator<T> implements Iterator<T>
	{

		private Entry<T> current;

		/**
		 * Constructor.
		 * @param first
		 */
		public SparseIterator(Entry<T> first) {
			this.current = first;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return current.next != null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public T next() {
			this.current = current.next;
			return this.current.object;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			
			Entry<T> previous = current.previous;
			Entry<T> next = current.next;
			
			next.previous = previous;
			previous.next = next;
			
		}
		
	}
	
	private class SparseListIterator<T> implements ListIterator<T>
	{

		private Entry<T> current;

		/**
		 * Constructor.
		 * @param it
		 * @param last
		 * @param index
		 */
		public SparseListIterator(Entry<T> current) {
			this.current = current.previous;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void add(T obj) {
			throw new UnsupportedOperationException("Not implememented yet");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return current.next != null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasPrevious() {
			return current.previous != null;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public T next() {
			if (current.next == null){
				throw new NoSuchElementException ();
			}
			this.current = current.next;
			return this.current.object;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int nextIndex() {
			if (current.next!= null){
				return current.next.index;
			} else {
				return LinkedSparseList.this.size();
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public T previous() {
			if (current.previous == null){
				throw new NoSuchElementException ();
			}
			this.current  = this.current.previous;
			return this.current.object;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int previousIndex() {
			if (current.previous != null){
				return current.previous.index;
			} else {
				return -1;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			Entry<T> previous = current.previous;
			Entry<T> next = current.next;
			
			next.previous = previous;
			previous.next = next;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void set(T object) {
			this.current.object = object;
		}
	}
}
