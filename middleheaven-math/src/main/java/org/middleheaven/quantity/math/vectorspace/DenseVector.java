package org.middleheaven.quantity.math.vectorspace;

import java.util.Arrays;
import java.util.List;

import org.middleheaven.collections.enumerable.FastCountEnumerable;
import org.middleheaven.quantity.math.structure.FieldElement;

/**
 * Dense implementation of Vector
 * 
 *
 * @param <F>
 */
public class DenseVector<F extends FieldElement<F>> extends AbstractVector<F>  implements FastCountEnumerable {


	private Object[] elements;

	public DenseVector(DenseVectorSpace<F> space){
		super(space);
		this.elements = new Object[space.getDimensions()];

	}

	public DenseVector(DenseVectorSpace<F> space, F value){
		this(space);

		if ( value!=null){
			Arrays.fill(this.elements, value);
		}
	}

	public DenseVector(DenseVectorSpace<F> space, List<F> values){
		this(space);

		int i=0;
		for (F value : values){
			elements[i] = value;
			i++;
		}
	}
	
	public DenseVector(DenseVector<F> other) {
		this((DenseVectorSpace<F>) other.getVectorSpace());
		
		for (int i=0;  i< other.size(); i++){
			elements[i] =other.get(i);
		}
	}

	public DenseVector<F> set(int index, F value){
		this.elements[index] =  value;
		return this;
	}


	@SuppressWarnings("unchecked")
	@Override
	public final F get(int index) {
		return (F) this.elements[index];
	}

	@Override
	public final int size() {
		return this.elements.length;
	}








}
