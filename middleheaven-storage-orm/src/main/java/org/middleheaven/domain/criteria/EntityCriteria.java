package org.middleheaven.domain.criteria;

import java.util.Collection;
import java.util.List;

import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.Criteria;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.OrderingCriterion;


public interface EntityCriteria<T> extends Criteria<T> {
	
    /**
     * If set to <code>true</code> limits the search to distinct instances.
     * Instances ares distinct if all their fields are distinct 
     */
    public EntityCriteria<T> setDistinct(boolean distinct);
    
    /**
     * 
     * @return <code>true</code> if only distinct instances are
     *  to be retrieved, <code>false</code> otherwise
     */
    public boolean isDistinct();


    /**
     * Retrieves a read-only list of <code>OrderingCriterion</code>
     * @return
     */
	public List<OrderingCriterion> ordering();
	
	/**
	 * Returns target entity class. Target entity is the one resulting from the query
	 * @return
	 */
	public Class<T> getTargetClass();
	
	public Class<T> getFromClass();
	
	/**
	 * Adds restrictions criterion
	 * @param criterion
	 */
	public EntityCriteria<T> add(Criterion criterion);

	/**
	 * quantity of instances to retrieve. negative numbers or zero means all
	 * @return quantity of instances to retrieve
	 */
	public int getCount();

	/**
	 * The index of the first instance from the full retrieved list. Zero is the first.
	 * @return the index of the first instance from the full retrieved list
	 */
	public int getStart();

	public EntityCriteria<T> setRange(int count);
	
	/**
	 * Set the first
	 * @param start
	 * @param count
	 * @return
	 */
	public EntityCriteria<T> setRange(int start, int count);

	/**
	 * Produces a query that retrieves only the key for the instance.
	 * @param b
	 */
	public void setKeyOnly(boolean b);
	
	public EntityCriteria<T> add(OrderingCriterion order);

	public Collection<QualifiedName> resultFields();

	/**
	 * Creates a duplicate of this criteria that can be edited idenpendently
	 * @return  a duplicate of this criteria. 
	 */
	public EntityCriteria<T> duplicate();

	public void setCountOnly(boolean b);

	public boolean isCountOnly();


}
