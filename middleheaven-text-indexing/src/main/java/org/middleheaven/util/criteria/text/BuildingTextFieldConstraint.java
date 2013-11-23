package org.middleheaven.util.criteria.text;

import org.middleheaven.collections.interval.Interval;
import org.middleheaven.text.indexing.IndexableDocument;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.criteria.AbstractBuildingConstraint;

class BuildingTextFieldConstraint<D extends IndexableDocument, B extends AbstractTextCriteriaBuilder<D,B>> 
extends AbstractBuildingConstraint<D, B>
implements TextFieldConstraint<D,B> {


	protected BuildingTextFieldConstraint(B builder,QualifiedName qualifiedFileName) {
		super(builder, qualifiedFileName);
	}

	@Override
	public TextFieldConstraint<D, B> not() {
		this.toogleNegate();
		return this;
	}

	public <V extends Comparable<? super V>> B bewteen(V min, V max) {
		return in(Interval.between(min, max));
	}
	
}
