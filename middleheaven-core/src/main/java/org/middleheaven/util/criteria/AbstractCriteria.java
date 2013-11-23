package org.middleheaven.util.criteria;

import org.middleheaven.util.classification.LogicOperator;


public abstract class AbstractCriteria<T> implements Criteria<T> {

	private LogicCriterion restrictions = new LogicCriterion(LogicOperator.and());
	
	@Override
	public Criteria<T> add(Criterion criterion) {
		this.restrictions.add(criterion);
		return this;
	}

	@Override
	public LogicCriterion constraints(){
		return restrictions;
	}

	protected final void setRestrictions(LogicCriterion restrictions){
		this.restrictions = restrictions;
	}
	
}
