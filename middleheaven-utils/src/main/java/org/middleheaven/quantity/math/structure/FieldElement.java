package org.middleheaven.quantity.math.structure;

/**
 * Represent a mathematical Field.
 * 
 * @param <F> the type of element on the field.
 */
public interface FieldElement<F extends FieldElement<F>> extends GroupMultiplicative<F>, RingElement<F>{

	/**
	 * 
	 * {@inheritDoc}
	 */
	public Field<F> getAlgebricStructure();
	
	public F abs();
}
