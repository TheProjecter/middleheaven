package org.middleheaven.util.criteria;



public interface FieldInSetCriterion extends FieldCriterion{

	boolean isIncluded();

	boolean useCriteria();

	public CollectionFieldValueHolder valueHolder();
}
