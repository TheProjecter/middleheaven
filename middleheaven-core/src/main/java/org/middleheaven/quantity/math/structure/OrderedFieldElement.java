package org.middleheaven.quantity.math.structure;

public interface OrderedFieldElement<F extends OrderedFieldElement<F>> extends FieldElement<F> {

	public abstract int compareTo(F other);

	/**
	 * 
	 * {@inheritDoc}
	 */
	public OrderedField<F> getAlgebricStructure();
}
