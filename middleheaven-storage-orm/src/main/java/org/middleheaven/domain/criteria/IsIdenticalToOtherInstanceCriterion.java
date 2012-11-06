package org.middleheaven.domain.criteria;

import org.middleheaven.util.criteria.Criterion;

public class IsIdenticalToOtherInstanceCriterion implements Criterion {

	private Object instance;

	public IsIdenticalToOtherInstanceCriterion(Object instance) {
		this.instance = instance;
	}

	public Object getInstance(){
		return instance;
	}
	
	
	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Criterion simplify() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJunction() {
		return false;
	}
}
