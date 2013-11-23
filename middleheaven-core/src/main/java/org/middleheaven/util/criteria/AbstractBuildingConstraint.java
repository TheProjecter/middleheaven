package org.middleheaven.util.criteria;

import java.util.Collection;

import org.middleheaven.collections.interval.Interval;
import org.middleheaven.util.QualifiedName;


public abstract class AbstractBuildingConstraint<T,B extends AbstractCriteriaBuilder<T,B>> {

	private B builder;
	private boolean negateFlag = false;
	private QualifiedName qname;


	protected AbstractBuildingConstraint (B builder, QualifiedName qualifiedFileName){
		this.qname = qualifiedFileName;
		this.builder = builder;
	}
	
	
	
	public B getBuilder() {
		return builder;
	}



	public QualifiedName getQualifiedName() {
		return qname;
	}



	protected final void toogleNegate() {
		this.negateFlag = !this.negateFlag;
	}
	
	protected CriterionOperator applyOperationNegation(CriterionOperator op){
		if (negateFlag){
			negateFlag=false;
			return op.negate();
		}
		return op;
	}
	
	protected B constrainField(CriterionOperator op, Object value){

		builder.addCriterion(
				new FieldValueCriterion(
						qname,
						applyOperationNegation(op),
						new SingleObjectValueHolder(value)
				)
		);
		return builder;
	}
	
	public B contains(CharSequence text) {
		return constrainField(CriterionOperator.CONTAINS, text);
	}

	public B endsWith(CharSequence text) {
		return constrainField(CriterionOperator.ENDS_WITH, text);
	}

	public B startsWith(CharSequence text) {
		return constrainField(CriterionOperator.STARTS_WITH, text);
	}

	public B near(CharSequence text) {
		return constrainField(CriterionOperator.NEAR, text);
	}
	
	public B eq(Object value) {
		return constrainField(CriterionOperator.EQUAL,value);
	}

	
	public B ge(Object value) {
		return constrainField(CriterionOperator.GREATER_THAN_OR_EQUAL,value);
	}

	
	public B gt(Object value) {
		return constrainField(CriterionOperator.GREATER_THAN,value);
	}

	
	public B le(Object value) {
		return constrainField(CriterionOperator.LESS_THAN_OR_EQUAL,value);
	}

	
	public B lt(Object value) {
		return constrainField(CriterionOperator.LESS_THAN,value);
	}
	
	public <V extends Comparable<? super V>> B bewteen(V min, V max) {
		return in(Interval.between(min, max));
	}
	
	public <V extends Comparable<? super V>> B in(Interval<V> interval) {
		return constrainField(CriterionOperator.IN, interval);
	}

	public B isNull() {
		return constrainField(CriterionOperator.IS_NULL,null);
	}

	public <V> B in(Collection<V> values) {
		CriterionOperator op = CriterionOperator.IN;
		if (negateFlag){
			negateFlag=false;
			op = op.negate();
		}

		builder.addCriterion(
				new CollectionFieldInSetCriteria(
						qname,
						op,
						values
				)
		);
		return builder;

	}

}
