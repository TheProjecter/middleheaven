/**
 * 
 */
package org.middleheaven.domain.criteria;

import java.util.Arrays;

import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.AbstractBuildingConstraint;

class BuildingEntityFieldConstraint<T,B extends AbstractEntityCriteriaBuilder<T, B>> 
extends AbstractBuildingConstraint<T,B> 
implements EntityFieldConstraint<T, B> {

	protected BuildingEntityFieldConstraint(B builder,QualifiedName qualifiedFileName) {
		super(builder, qualifiedFileName);
	}


	@Override
	public EntityFieldConstraint<T,B> not() {
		this.toogleNegate();
		return this;
	}

	@Override
	public <V> B in(V... values) {
		return in(Arrays.asList(values));
	}

	@Override
	public <O> B is(O candidate) {
		if (candidate == null){
			return this.isNull();
		} else {
			return navigateTo(Introspector.of(candidate).getRealType())
			.is(candidate)
			.back();
		}
	}

	@Override
	public <N> JunctionCriteriaBuilder<N, T , B> navigateTo(Class<N> referencedEntityType) {
		FieldJuntionCriterion criterion = new FieldJuntionCriterion(this.getQualifiedName(),referencedEntityType,getBuilder().getCurrentType(), true);
		getBuilder().addCriterion(criterion);
		return new JunctionCriteriaBuilder<N,T,B>(criterion,referencedEntityType,getBuilder());
	}


	@Override
	public <N> JunctionCriteriaBuilder<N, T, ?> navigateFrom(
			Class<N> referencedEntityType) {
		
		FieldJuntionCriterion criterion = new FieldJuntionCriterion(this.getQualifiedName(),referencedEntityType,getBuilder().getCurrentType(), false);
		getBuilder().addCriterion(criterion);
		return new JunctionCriteriaBuilder<N,T,B>(criterion,referencedEntityType,getBuilder());
	}




}