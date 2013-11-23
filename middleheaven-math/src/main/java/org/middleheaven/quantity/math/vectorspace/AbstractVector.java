package org.middleheaven.quantity.math.vectorspace;

import java.util.Collection;
import java.util.Iterator;

import org.middleheaven.collections.enumerable.AbstractEnumerable;
import org.middleheaven.collections.iterators.IndexBasedIterator;
import org.middleheaven.quantity.math.Conjugatable;
import org.middleheaven.quantity.math.UnivariateFunction;
import org.middleheaven.quantity.math.structure.FieldElement;
import org.middleheaven.quantity.math.structure.GroupAdditive;
import org.middleheaven.util.function.Block;

public abstract class AbstractVector<F extends FieldElement<F>> extends AbstractEnumerable<F> implements Vector<F> , Conjugatable<Vector<F>>{
	
	
	private final VectorSpace<Vector<F>, F > vectorSpace;

	protected AbstractVector (VectorSpace<Vector<F>, F > vectorSpace){
		this.vectorSpace = vectorSpace;
		
	}
	
	
	@SuppressWarnings("unchecked")
	protected Vector<F> cross(Vector<F> other){
		if (this.size()!=3 || this.size()!=3){
			throw new ArithmeticException("Cross product it not defined in this space");
		}
		
		return vectorSpace.vector(
				this.get(2).times(this.get(3)).minus(this.get(3).times(this.get(2))),
				this.get(3).times(this.get(1)).minus(this.get(1).times(this.get(3))),
				this.get(1).times(this.get(2)).minus(this.get(2).times(this.get(1)))
		);
		
		
	}
	

	@Override
	public GroupAdditive<Vector<F>> getAlgebricStructure() {
		return new GroupAdditive<Vector<F>>(){

			@Override
			public boolean isGroupAdditive() {
				return true;
			}

			@Override
			public boolean isRing() {
				return false;
			}

			@Override
			public boolean isField() {
				return false;
			}

			@Override
			public Vector<F> zero() {
				return vectorSpace.replicate(vectorSpace.getField().zero());
			}
			
		};
	}
	
	
	public VectorSpace<Vector<F>, F> getVectorSpace(){
		return vectorSpace;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector<F> multiply(final Vector<F> other) {
		return new LazyDelegationVector<F>(this, new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).times(other.get(i));
			}

		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final F times(Vector<F> other){
		if (this.size()!= this.size()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		
		F result = this.get(0).times(other.get(0));
		for (int i =1; i <this.size(); i++){
			result = result.plus((this.get(i).times(other.get(i))));
		}
		return result;
	}

	@Override
	public Vector<F> times(final F a) {

		return new LazyDelegationVector<F>(this, new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).times(a);
			}

		});
		
	}

	@Override
	public final Vector<F> minus(final Vector<F> other) {
		if (this.size()!= this.size()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		
		return new LazyDelegationVector<F>(this,  new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).minus(other.get(i));
			}

		});
	}

	@Override
	public Vector<F> negate() {
		
		return new LazyDelegationVector<F>(this,  new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).negate();
			}

		});

	}

	@Override
	public Vector<F> plus(final Vector<F> other) {
		if (this.size()!= this.size()){
			throw new ArithmeticException("Dimentions must be equals");
		}
		
		return new LazyDelegationVector<F>(this, new ValueResolver<F>(){

			@Override
			public F resolve(int i, Vector<F> original) {
				return original.get(i).plus(other.get(i));
			}

		});
		
	}


	public final Vector<F> conjugate() {
		
		return new LazyDelegationVector<F>(this, new ValueResolver<F>(){

			@SuppressWarnings("unchecked")
			@Override
			public F resolve(int i, Vector<F> original) {
				F value = original.get(i);
				if (value instanceof Conjugatable){
					return ((Conjugatable<F>) value).conjugate();
				} else {
					return value;
				}
			}

		});

	}

	@SuppressWarnings("unchecked")
	public boolean equals (Object other){
		return other instanceof Vector && equalsOther((Vector<F>)other);
	}

	protected boolean equalsOther(Vector<F> other){
		for (int i=0; i < this.size(); i++){
			if (!this.get(i).equals(other.get(i))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return this.size();
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder("[");
		
		for (int i=0; i < this.size(); i++){
			builder.append(this.get(i)).append( ",");
		}
		
		return builder.append("]").toString();
	}




	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<F> iterator() {
		return new IndexBasedIterator<F>(){

			@Override
			protected int getSize() {
				return size();
			}

			@Override
			protected F getObject(int index) {
				return get(index);
			}
			
		};
	}

	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void forEach(Block<F> walker) {
		
		if (this.size() != 0){
			for (int i=0; i < this.size(); i++){
				walker.apply(this.get(i));
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <L extends Collection<F>> L into(L collection) {
		for (int i=0; i < this.size(); i++){
			collection.add(this.get(i));
		}
		return collection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZero() {
		return this.equals(this.getAlgebricStructure().zero());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <N extends FieldElement<N>> Vector<N> apply(final UnivariateFunction <F, N> function) {
		
		return new LazyProxyVector<N>(this.vectorSpace, this.size()) {

			@Override
			public N lazyGet(int index) {
				return function.apply(AbstractVector.this.get(index));
			}


		};
	}

}
