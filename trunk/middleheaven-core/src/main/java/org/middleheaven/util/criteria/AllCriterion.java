package org.middleheaven.util.criteria;

public class AllCriterion implements Criterion {

	private static final long serialVersionUID = -5297475726331343866L;
	private static final AllCriterion me = new AllCriterion();
	
	public static AllCriterion all(){
		return me;
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
