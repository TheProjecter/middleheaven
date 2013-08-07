/**
 * 
 */
package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.structure.Field;
import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * 
 */
public final class DenseVectorSpaceProvider implements VectorSpaceProvider {

	private static final long serialVersionUID = 7559322914362799713L;

	private static final DenseVectorSpaceProvider ME = new DenseVectorSpaceProvider();
	
	public static DenseVectorSpaceProvider getInstance(){ 
		return ME;
	}
	
	private DenseVectorSpaceProvider(){}

	@Override
	public <F extends FieldElement<F>> DenseVectorSpace<F> getVectorSpaceOver(Field<F> field, int dimensions) {
		return new DenseVectorSpace<F>(dimensions, field);
	}
}
