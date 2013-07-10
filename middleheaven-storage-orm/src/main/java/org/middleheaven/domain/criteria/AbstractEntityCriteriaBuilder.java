package org.middleheaven.domain.criteria;

import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.AbstractCriteriaBuilder;
import org.middleheaven.util.criteria.BuildingOrdering;
import org.middleheaven.util.criteria.Criterion;
import org.middleheaven.util.criteria.OrderingConstrain;
import org.middleheaven.util.criteria.OrderingCriterion;


public abstract class AbstractEntityCriteriaBuilder<T , B extends AbstractEntityCriteriaBuilder<T,B>> extends AbstractCriteriaBuilder<T,B> {

	private static class BuildedCriteria<T> extends AbstractEntityCriteria<T> {

		public BuildedCriteria(BuildedCriteria<T> other) {
			super(other);
		}

		public BuildedCriteria(Class<T> type) {
			super(type);
		}

		@Override
		public EntityCriteria<T> duplicate() {
			return new BuildedCriteria<T>(this);
		}

	}
	
	protected AbstractEntityCriteria<T> criteria;

	public EntityCriteria<T> all() {
		return this.criteria;
	}
	
	protected AbstractEntityCriteriaBuilder(Class<T> type){
		this.criteria = new BuildedCriteria<T>(type);
	}

	private QualifiedName qualify(String name){
		return QualifiedName.qualify(this.getCurrentType().getSimpleName().toLowerCase(), name);
	}
	
	public B hasIdentity(Object identity) {
		if (identity == null){
			throw new IllegalArgumentException("Identity is required. The identity is never null");
		}

		this.criteria.add(new IdentityCriterion(identity));
		
		return me();
	}

	/**
	 * The current navigation target entity is the same as the one given
	 * @param instance the given instance of an entity
	 * @return the current builder.
	 */
	public B is(Object instance){
		if (instance == null){
			throw new IllegalArgumentException("An instance is required.");
		}
		
		this.criteria.add(new EqualsOtherInstanceCriterion(this.criteria.getTargetClass(), instance));
		
		return me();
		
	}


	/**
	 * Starts a constraint on a field.
	 * @param name name of the field.
	 * @return
	 */
	public EntityFieldConstraint<T,B> and(String name) {
		return new BuildingEntityFieldConstraint<T,B>(me(),qualify(name));
	}

	private B me(){
		return (B) this;
	}


	public EntityFieldConstraint<T,B> or(String name) {
		return new BuildingEntityFieldConstraint<T,B>(me(),qualify(name));
	}
	
	public OrderingConstrain<T,B> orderBy(String name) {
		return new BuildingOrdering<T,B>(me(),qualify(name));
	}

	protected void addCriterion(Criterion criterion){
		this.criteria.add(criterion);
	}

	protected void addOrderingCriterion(OrderingCriterion criterion){
		this.criteria.add(criterion);
	}
	
	protected Class<T> getCurrentType(){
		return this.criteria.getTargetClass();
	}
}
