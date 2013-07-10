package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.UnivariateFunction;
import org.middleheaven.quantity.math.structure.FieldElement;

public class DenseMatrix<F extends FieldElement<F>> extends AbstractMatrix<F> {


	protected Object[] all;

	private int columnsCount;
	private int rowsCount;

	/**
	 * The columnsCount is compatible with the size of the vector space
	 * Constructor.
	 * @param rowsCount
	 * @param columnsCount
	 * @param value
	 */
	DenseMatrix(int rowsCount, int columnsCount, F value){
		super(DenseVectorSpaceProvider.getInstance().getVectorSpaceOver(value.getAlgebricStructure(), columnsCount));
		this.rowsCount = rowsCount;
		this.columnsCount = columnsCount;
		all = new Object[rowsCount * columnsCount];

		for (int i = 0; i < this.rowsCount * columnsCount; i++){
			all[i] = value;
		}
	}

	public DenseMatrix(Matrix<F> other){
		super(DenseVectorSpaceProvider.getInstance().getVectorSpaceOver(other.getField(), other.columnsCount()));
		this.rowsCount = other.rowsCount();
		this.columnsCount = other.columnsCount();
		all = new Object[rowsCount * columnsCount];

		for (int r = 0; r < this.rowsCount; r++){
			for (int c = 0; c < this.columnsCount; c++){
				this.set(r, c, other.get(r, c));
			}
		}
	}



	public DenseMatrix(Matrix<F> other, int ri, int c0, int rowCount, int columnCount) {
		this(rowCount,columnCount, other.getField().zero());

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				this.set(i, j , other.get(ri+i , c0+j));
			}
		}

	}

	public DenseMatrix(Matrix<F> other, int[] r, int j0, int j1) {
		this(r.length,j1-j0+1, other.getField().zero());

		for (int i = 0; i < r.length; i++) {
			for (int j = j0; j <= j1; j++) {
				this.set(i, j-j0 , other.get(r[i] , j));
			}
		}

	}



	public DenseMatrix<F> set(int r, int c, F value){

		all[indexOf(r,c)] = value;
		return this;
	}


	public DenseMatrix<F> setByIndex(int index, F value){

		all[index] = value;
		return this;
	}

	private int indexOf(int row, int column){
		return row * this.rowsCount + column;
	}

	@Override
	public final int columnsCount() {
		return columnsCount;
	}

	@Override
	public final int rowsCount() {
		return rowsCount;
	}

	@Override
	public final F get(int r, int c) {
		return (F) all[indexOf(r,c)];
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <N extends FieldElement<N>> Matrix<N> apply(
			UnivariateFunction<F, N> function) {
		throw new UnsupportedOperationException("Not implememented yet");
	}























}
