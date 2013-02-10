/**
 * 
 */
package org.middleheaven.persistance.criteria.building;

import org.middleheaven.persistance.criteria.DataSetCriteria;
import org.middleheaven.persistance.model.TypeDefinition;

/**
 * 
 */
public interface WhereTermBuilder {

	/**
	 * @param date
	 * @return
	 */
	ConditionTermComparisonOperatorBuilder column(TypeDefinition<?> date);
	
	/**
	 * @return the {@link ConditionaRestrictionlBuilder}
	 */
	ConditionaRestrictionlBuilder or();

	/**
	 * @return
	 */
	ConditionaRestrictionlBuilder and();

	/**
	 * @return
	 */
	DataSetCriteria all();


	/**
	 * @return the {@link ColumnSetBuilder}
	 */
	ColumnSetBuilder groupBy();
}
