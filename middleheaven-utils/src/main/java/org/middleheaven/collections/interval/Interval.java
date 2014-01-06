package org.middleheaven.collections.interval;

import java.util.Comparator;

import org.middleheaven.collections.ComparableComparator;
import org.middleheaven.util.Hash;



/**
 * Provides mathematical interval semantics and operations 
 * 
 * @author Sergio M.M. Taborda
 */
public class Interval<T> {

	protected T start;
	protected T end;
	protected Comparator<? super T> comparator;


	public final static <V extends Comparable<? super V>> Interval<V> emptyInterval(){
		return new Interval<V>(ComparableComparator.<V>getInstance());
	}

	public final static <V extends Comparable<? super V>> Interval<V> emptyInterval(Comparator<? super V> comparator){
		return new Interval<V>(comparator);
	}
	

	/**
	 * Creates a Interval from the values of two comparable objects. 
	 * @param start start of the Interval
	 * @param end end of the Interval
	 * @throws IllegalArgumentException if start is not less than end or their are null
	 * @throws ClassCastException if the values are not <code>Comparable</code>.
	 * @return a <code>Interval</code> from <code>start</code> to <code>end</code>
	 */
	public final static <V extends Comparable<? super V>> Interval<V> between(V start, V end){
		return between(start,end,ComparableComparator.<V>getInstance());
	} 

	/**
	 * Creates a Interval from the values of two objects and a <code>Comparator</code> for those objects.
	 * @param <V>
	 * @param start start of the Interval
	 * @param end end of the Interval
	 * @param comparator <code>Comparator</code> encapsulating the rules of order for the passed object class
	 * @return a <code>Interval</code> from <code>start</code> to <code>end</code>
	 */
	public static <V> Interval<V> between(V start, V end, Comparator<? super V> comparator){
		if (comparator == null){
			throw new IllegalArgumentException("A comparator is required.");
		}
		if (start!=end && start!=null && end!=null && comparator.compare(start,end)>0){
			throw new IllegalArgumentException("Interval`s start must preceed its end");
		}

		return new Interval<V>(start,end,comparator);
	} 
	

    Interval(T start, T end,Comparator<? super T> comparator) {
		
		this.start = start;
		this.end = end;
		this.comparator = comparator;
	}

	/**
	 * @param start
	 * @param end
	 * @param comparator
	 */
   protected Interval(Comparator<? super T> comparator) {
		this.start = null;
		this.end = null;
		this.comparator = comparator;
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
	 * The interval is empty if the start and end point are equal
	 * @return <code>true</code> if the interval is empty.
	 */
	public boolean isEmpty(){
		// first comparison is necessary as start and end can be null
		return start == end || start.equals(end);
	}



	/**
	 * Determines if this interval intersects another
	 * @param other the candidate to intersection
	 * @return <code>true</code> if <code>this</code> intersects <code>other</code>
	 */
	public boolean intersects (Interval<T> other){

		return !this.isEmpty() && !other.isEmpty() &&
		(this.contains(other.start, true, true) || 
				this.contains(other.end, true, true) || 
				other.contains(this.start, true, true) || 
				other.contains(this.end, true, true));


	}

	/**
	 * Determines this interval intersection with another
	 * @param other the interval to intersect
	 * @return an interval representing the intersections of the intervals
	 */
	public Interval<T> intersection (Interval<T> other){

		// is one is empty the intersection is empty
		if (this.isEmpty() || other.isEmpty()) {
			return new Interval<T>(this.comparator);
		}

		if (other.equalsOther(this)){ // are the same. 
			return other;
		}

		if (this.intersects(other)) {
			// intersects
			return new Interval<T>( max(this.start, other.start), min(this.end, other.end) , comparator);
		} else {
			// does not intersect
			return new Interval<T>(this.comparator);
		}


	}

	/**
	 * The union of this Interval with an another. 
	 * The result is a Interval from the lowest start to the greatest end
	 * Note that union is a commutative operation,
	 * meaning that R.union(S).equals(S.union(R)) == true , R and S being instances of Interval.
	 * @param other
	 * @return a Interval from the lowest start to the greatest end. Example : [2,3] merged with [7,10]  will return [2,10] 
	 */
	public Interval<T> union (Interval<T> other){
		return new Interval<T> ( 
				min(this.start, other.start) , // start at the minor start
				max(this.end , other.end), // end at the major end
				this.comparator // the same comparator
		);
	}


	/**
	 * Determines if an object is between the start(inclusive) and end(inclusive) of the interval
	 * @param other the candidate object
	 * @return <code>true</code> if the object is in the interval , <code>false</code> otherwise
	 */
	public boolean contains(T value){
		return !this.isEmpty() && comparator.compare(value, start) >=0 && comparator.compare(value, end) <=0;
	}


	/**
	 * Determine if the value is contained in the Interval considering the start and the end 
	 * of the Interval as belonging, or not , to the Interval according with openLeft and openRight arguments
	 * @param value the value to test
	 * @param includeStart if <code>true<code> test assuming the start of the interval does belongs to the Interval.
	 * @param includeEnd if <code>true<code> test assuming the end of the interval does belongs to the Interval.
	 * @return
	 */
	public boolean contains(T value, boolean includeStart , boolean includeEnd){
		return !this.isEmpty() 
		&& (start==null ? true : (includeStart?comparator.compare(value, start) >=0: comparator.compare(value, start) >0 )) 
		&& (end == null ? true : (includeEnd?comparator.compare(value, end) <=0:comparator.compare(value, end) <0));
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
	 * Intervals are equal if the their starts are equal and their ends are equals
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean equals(Object other){
		return other instanceof Interval && this.equalsOther((Interval)other);
	}
	
    public int hashCode(){
    	return Hash.hash(start).hash(end).hashCode();
    }

	/**
	 * Intervals are equal if their starts are equal and their ends are equals
	 */
	private boolean equalsOther(Interval<T> other){
		return (this.isEmpty() && other.isEmpty() ) || // both are empty or the limits are equal
		( comparator.compare(start, other.start)==0 && comparator.compare(end, other.end)==0);
	}



}
