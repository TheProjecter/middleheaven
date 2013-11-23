package org.middleheaven.util.criteria;

import org.middleheaven.util.QualifiedName;

public interface FieldCriterion extends Criterion {

	public QualifiedName getFieldName();
	public FieldValueHolder valueHolder();
	public CriterionOperator getOperator();
}
