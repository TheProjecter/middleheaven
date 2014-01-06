/*
 * Created on 2006/09/22
 *
 */
package org.middleheaven.util.function;



/**
 * Filter that returns the opposite result  of a underlying filter.
 *
 */
public class NegatedPredicate<T> implements Predicate<T> {


	private Predicate<T>  original;

	public NegatedPredicate(){};

	public NegatedPredicate(Predicate<T>  original){
		this.original = original;
	}

	public void setFilter(Predicate<T> filter){
		this.original = filter;
	}

	public Predicate<T>  getFilter(){
		return original;
	}

	@Override
	public Boolean apply(T obj) {
		return !original.apply(obj);
	}
		
		
}


