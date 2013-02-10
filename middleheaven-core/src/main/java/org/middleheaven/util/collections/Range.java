package org.middleheaven.util.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import org.middleheaven.quantity.math.structure.GroupAdditiveElement;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.util.Hash;
import org.middleheaven.util.Incrementor;


/**
 * Provides range semantics and operations.
 * A {@link Range} is like an {@link Interval} with iterator semantics over the interval values.
 * An {@link Incrementor} is used to define how the iteration visits the values in the interval.
 * 
 * @param <T> the type of the value in the range.
 * @param <I> type of the incremente. Normally is the same as T, but not allways( i.e. dates have increment as integers)
 */
public class Range<T, I> extends AbstractEnumerable<T> implements RandomEnumerable<T> {

	private final Comparator<? super T> comparator;
	private final Incrementor<? super T, ? super I> incrementor;
	private final T start;
	private final T end;
	private boolean isReversed = false;
	protected boolean excludeStart = false;
	protected boolean excludeEnd = false;

	
	/**
	 * An empty {@link Range}.
	 * @param <V> type of the element in the range.
	 * @return An empty {@link Range}
	 */
	public static <V extends Comparable<V>, I> Range<V, I> emptyRange(){
		final Incrementor<V, I> emptyIncrementor = EmptyIncrementor.<V,I>emptyIncrementor();
		Range<V, I> empty = new Range<V,I>(ComparableComparator.<V>getInstance(), emptyIncrementor);
		empty.excludeEnd = true;
		empty.excludeStart = true;
		
		return empty;
	}
	
	/**
	 * Starts the construction of a range from the start value, that cannot be null.
	 * 
	 * @param start the star t value.
	 * @return a {@link RangeBuilder}.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <V extends Comparable<? super V>, I> RangeBuilder<V, I> from(V start){
		
		if (start == null){
			throw new IllegalArgumentException("Range start cannot be null");
		}
		if (start instanceof Number){
			return new NumberRangeBuilder((Number) start);
		} else if (Character.class.isInstance(start)){
			return (RangeBuilder<V, I>) new CharacterRangeBuilder(Character.class.cast(start));
		} else if (start instanceof GroupAdditiveElement){
			return (RangeBuilder<V, I>) new GroupAdditiveRangeBuilder((GroupAdditiveElement) start);
		} else if (CalendarDate.class.isInstance(start)){
			return (RangeBuilder<V, I>) new CalendarDateRangeBuilder(CalendarDate.class.cast(start));
		}
		throw new IllegalArgumentException("Can not build a range for type " + start.getClass()); // TODO generic builder
	}
	

	Range(T start, T end,Comparator<? super T> comparator,Incrementor<? super T,? super I> incrementor) {
		
		this.start = start;
		this.end = end;
		this.comparator = comparator;
		
		if (start!= null && end != null && comparator.compare(start,end)>0){
			// the ordering ir reversed, so reverse the incrementor
			incrementor = incrementor.reverse();
			isReversed = true;
		}

		this.incrementor = incrementor;
	}

	/**
	 * @param start
	 * @param end
	 * @param comparator
	 */
	Range(Comparator<? super T> comparator,Incrementor<? super T, ? super I> incrementor) {
		this(null,null,comparator, incrementor);
	}

	public Range<T, I> incrementBy(I step){
		Range<T, I> range = new Range<T, I>(this.start, this.end, this.comparator, this.incrementor);
		
		range.excludeEnd = this.excludeEnd;
		range.excludeStart = this.excludeStart;
		
		return range;
	}
	
	public Range<T, I> excludeStart(){
		Range<T, I> range = new Range<T, I>(this.start, this.end, this.comparator, this.incrementor);
		
		range.excludeStart = true;
		
		return range;
	}
	
	public Range<T, I> excludeEnd(){
		Range<T, I> range = new Range<T, I>(this.start, this.end, this.comparator, this.incrementor);
		
		range.excludeEnd = true;
		
		return range;
	}
	
	/**
	 * Determines this interval intersection with another
	 * @param other the interval to intersect
	 * @return an interval representing the intersections of the intervals
	 */
	public Range<T, I> intersection (Range<T, I> other){

		// is one is empty the intersection is empty
		if (this.isEmpty() || other.isEmpty()) {
			return new Range<T, I>(this.comparator,this.incrementor);
		}

		if (other.equals(this)){ // are the same. 
			return this;
		}

		if (this.intersects(other)) {
			// intersects
			return new Range<T, I>( max(this.start, other.start), min(this.end, other.end) , comparator, incrementor);
		} else {
			// does not intersect
			return new Range<T, I>(this.comparator,incrementor);
		}

	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean isEmpty(){
		if (start == end || comparator.compare(start, end) == 0){
			if (this.excludeEnd || this.excludeStart) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean contains(T value, boolean includeStart , boolean includeEnd){
		if (this.isReversed) {
			return !this.isEmpty() 
					&& (start==null ? true : (includeStart?comparator.compare(value, start) <=0: comparator.compare(value, start) <0 )) 
					&& (end == null ? true : (includeEnd?comparator.compare(value, end) >=0:comparator.compare(value, end) >0));
		} else {
			return !this.isEmpty() 
					&& (start==null ? true : (includeStart?comparator.compare(value, start) >=0: comparator.compare(value, start) >0 )) 
					&& (end == null ? true : (includeEnd?comparator.compare(value, end) <=0:comparator.compare(value, end) <0));
		}

	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean contains(T value){
		if (comparator.compare(start, end) ==0 ){
			if (this.excludeEnd || this.excludeStart){
				return false;
			} else {
				return comparator.compare(value, end) ==0; 
			}
			
		} else {
			return contains(value, !this.excludeStart, !this.excludeEnd);
		}
		
	}
	
	/**
	 * The union of this range with an another. 
	 * The result is a range from the lowest start to the greatest end
	 * Note that union is a commutative operation,
	 * meaning that R.union(T).equals(T.union(R)) == true , R and T being instances of Range.
	 * @param other
	 * @return a range from the lowest start to the greatest end. Example : [2,3] merged with [7,10]  will return [2,10] 
	 */
	public Range<T, I> union (Interval<T> other){
		return new Range<T, I> ( 
				min(this.start, other.start) , // start at the minor start
				max(this.end , other.end), // end at the major end
				this.comparator, // the same comparator
				this.incrementor
		);
	}


	/**
	 * Ranges are equal if the their starts are equal and their ends are equals
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object other){
		if (other instanceof Range){
			return this.equalsRange((Range)other);
		} else if (other instanceof Interval){
			return this.equalsInterval((Interval)other);
		} else {
			return false;
		}
	}
	
	private boolean equalsInterval(Interval<T> other){
		return (this.isEmpty() && other.isEmpty() ) || // both are empty or the limits are equal
		( comparator.compare(start, other.start)==0 && comparator.compare(end, other.end)==0);
	}

    public int hashCode(){
    	return Hash.hash(start).hash(end).hashCode();
    }
	/**
	 * Ranges are equal if their starts are equal and their ends are equals
	 */
	private boolean equalsRange(Range<T, I> other){
		return (this.isEmpty() && other.isEmpty() ) || // both are empty or the limits are equal
		( comparator.compare(start, other.start)==0 && comparator.compare(end, other.end)==0);
	}

	@Override
	public Iterator<T> iterator() {
		return new RangeIterator(comparator,incrementor);
	}


	private int size = -1;
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int size(){
		if (this.isEmpty()){
			return 0;
		}
		
		if(size>=0){
			return size;
		}
		
		size=0;
		for (Iterator<T> it = this.iterator();it.hasNext();size++){
			it.next();
		}
		return size;
	}

    class RangeIterator implements Iterator<T> {

		T current;
		final Comparator<? super T> comparator;
		final Incrementor<? super T, ? super I> incrementor;

		@SuppressWarnings("unchecked")
		public RangeIterator(Comparator<? super T> comparator , Incrementor<? super T, ? super I> incrementor){
			this.comparator = comparator;
			this.incrementor = incrementor;
			
			if (excludeStart) {
				this.current = (T) incrementor.increment(Range.this.start);
			} else {
				this.current = Range.this.start;
			}
			
		}

		@Override
		public boolean hasNext() {
			return contains(current);
			//return (isReversed ?  comparator.compare(current, Range.this.end) >= 0 : comparator.compare(current, Range.this.end) <= 0);
		}

		@SuppressWarnings("unchecked")
		@Override
		public T next() {
			T toReturn = current;
			current = (T) incrementor.increment(current);
			return toReturn;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

    /**
     * Produce a random list of elements based on the list that corresponds to this range.
     * @return  a random list of elements based on the list that corresponds to this range.
     */
	public T random() {
		return index(( int )( Math.random() * this.size()));
	}

	/**
	 * Produce a random list of elements based on the list that corresponds to this range.
	 * @param random the {@link Random} where to obtain the random sequence.
	 * @return  a random list of elements based on the list that corresponds to this range.
	 */
	public T random(Random random) {
		return index(random.nextInt(this.size()));
	}

	private T index(int index){
		
		Iterator<T> it = this.iterator();
		for (int i = 0; i < index; i++){
			it.next();
		}
		
		return it.hasNext() ? null : it.next();
	}
	
	/**
	 * @return Returns the end.
	 */
	public T end() {
		return end;
	}

	/**
	 * @return Returns the start.
	 */
	public T start() {
		return start;
	}


	/**
	 * Determines if this interval intersects another
	 * @param other the candidate to intersection
	 * @return <code>true</code> if <code>this</code> intersects <code>other</code>
	 */
	public boolean intersects (Range<T, I> other){

		return !this.isEmpty() && !other.isEmpty() &&
		(this.contains(other.start, true, true) || 
				this.contains(other.end, true, true) || 
				other.contains(this.start, true, true) || 
				other.contains(this.end, true, true));


	}

	/**
	 * Determines the maximum of two values using the 
	 * comparator
	 * @param a
	 * @param b
	 * @return the maximum between a and b
	 */
	protected T  max(T a , T b){
		if (comparator.compare(a,b)>=0){
			return a;
		}
		return b;
	}

	/**
	 * Determines the minimum of two values using the 
	 * comparator
	 * @param a
	 * @param b
	 * @return the minimum between a and b
	 */
	protected T  min(T a , T b){
		if (comparator.compare(a,b)<=0){
			return a;
		}
		return b;
	}

	public String toString(){
		if (this.isEmpty()){
			return "[]";
		}
		return "[" + start.toString() + " ; " +  end.toString() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getFirst() {
		 return excludeStart ? (T)this.incrementor.increment(this.start()) : this.start();
	}


	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getLast() {
		 return this.excludeEnd ? (T)this.incrementor.reverse().increment(this.end()) :  this.end();
	}




}

