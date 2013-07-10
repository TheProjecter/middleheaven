package org.middleheaven.domain.criteria;

import java.util.Collection;

import org.middleheaven.collections.Interval;

/**
 * Extention for the CriteriaBuilder to support creation of fields 
 * constrains a fluente interface. 
 *
 * @param <T>
 */
public interface EntityFieldConstraint<T, B extends AbstractEntityCriteriaBuilder<T,B> > {

	/**
	 * The attribute value equals {@code value}. . 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B eq(Object value);  
	/**
	 * The attribute value is less than {@code value}. . 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B lt(Object value);  
	
	/**
	 * The attribute value is greater than {@code value}. . 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B gt(Object value);
	/**
	 * The attribute value is less than or equal to {@code value}. 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B le(Object value);  
	/**
	 * The attribute value is greater than or equal to {@code value}. 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B ge(Object value);
	/**
	 * The attribute value is {@code null} 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B isNull();
	/**
	 * The attribute value start with {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B startsWith(CharSequence text);
	/**
	 * The attribute value ends with {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B endsWith(CharSequence text);
	/**
	 * The attribute value contains {@code value}. This is only 
	 * applied if the atribute's value is a textual value 
	 * @param value value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public B contains(CharSequence text);
	
	/**
	 * The attribute value is contained in the {@code values} collection.
	 * @param values value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V> B in(Collection<V> values);
	
	/**
	 * The attribute value is contained in one of the given {@code values}.
	 * @param values value to be compared
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V> B in(V ... values);
	/**
	 * The attribute value is contained in the given {@code interval}.
	 * @param interval interval where the attribute's value must be contained
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V extends Comparable<? super V>> B in(Interval<V> interval);
	/**
	 * The attribute value is equal or greater than {@code min} and less or equal than {@code max}.
	 * @param <V>
	 * @param min minimum value
	 * @param max maximum value
	 * @return CriteriaBuilder<T> with the added constraint.
	 */
	public <V extends Comparable<? super V>> B bewteen(V min, V max);  

	/**
	 * 
	 * @return denies the constraint selected next. 
	 */
	public EntityFieldConstraint<T, B> not();
	
	/**
	 * The attribute value is an entity equals to {@code candidate}.
	 * @param <O>
	 * @param candidate candidate to be tested
	 * @return
	 */
	public <O> B is(O candidate);
	
	/**
	 * Transfers criteria building to the <code>referencedEntityType</code>.
	 * 
	 * @param <N> the referencedEntityType
	 * @param referencedEntityType
	 * @return
	 */
	public <N> JunctionCriteriaBuilder<N, T, ?> navigateTo(Class<N> referencedEntityType);
	
	/**
	 * Transfers criteria building to the <code>referencedEntityType</code>.
	 * 
	 * @param <N> the referencedEntityType
	 * @param referencedEntityType
	 * @return
	 */
	public <N> JunctionCriteriaBuilder<N, T, ?> navigateFrom(Class<N> referencedEntityType);
}
