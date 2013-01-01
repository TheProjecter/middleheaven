package org.middleheaven.util.criteria;

import java.util.Collection;

import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.collections.CollectionUtils;

class CollectionFieldInSetCriteria implements FieldInSetCriterion {


	private static final long serialVersionUID = -5681368324005300424L;
	
	Collection<?> values;
	private QualifiedName name;
	private CriterionOperator operator;
	
	/**
	 * 
	 * Constructor.
	 * @param name {@link QualifiedName} of the source field.
	 * @param operator the operator to use in the match
	 * @param values the values to match
	 */
	public CollectionFieldInSetCriteria(QualifiedName name , CriterionOperator operator , Collection<?> values ) {
		super();
		this.values = values;
		this.name = name;
		this.operator = operator;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJunction() {
		return false;
	}
	
	@Override
	public boolean isIncluded() {
		return operator.equals(CriterionOperator.IN);
	}

	@Override
	public boolean useCriteria() {
		return false;
	}

	@Override
	public QualifiedName getFieldName() {
		return name;
	}

	@Override
	public CriterionOperator getOperator() {
		return operator;
	}

	@Override
	public CollectionFieldValueHolder valueHolder() {
		return new CollectionFieldValueHolder(){

			@Override
			public boolean isEmpty() {
				return values.isEmpty();
			}

			@Override
			public boolean equalsValue(FieldValueHolder valueHolder) {
				if (valueHolder instanceof CollectionFieldValueHolder) {
					return CollectionUtils.equalContents(values, ((CollectionFieldValueHolder) valueHolder).getValue());
				}
				
				return false;
			}

			@Override
			public String getParam(String string) {
				return null;
			}

			@Override
			public void setParam(String string, String param) {

			}

			@Override
			public Collection getValue() {
				return values;
			}
			
		};
	}

	@Override
	public boolean isEmpty() {
		return values.isEmpty();
	}

	@Override
	public Criterion simplify() {
		return this;
	}


}
