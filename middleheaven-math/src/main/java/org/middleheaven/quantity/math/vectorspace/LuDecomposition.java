package org.middleheaven.quantity.math.vectorspace;

import org.middleheaven.quantity.math.structure.FieldElement;


/** LU Decomposition.
<P>
For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n
unit lower triangular matrix L, an n-by-n upper triangular matrix U,
and a permutation vector piv of length m so that A(piv,:) = L*U.
If m < n, then L is m-by-m and U is m-by-n.
<P>
The LU decomposition with pivoting always exists, even if the matrix is
singular, so the constructor will never fail.  The primary use of the
LU decomposition is in the solution of square systems of simultaneous
linear equations.  This will fail if {@link #isNonsingular()} returns <code>false</code>.
 */
public final class LuDecomposition<F extends FieldElement<F>> {


	private Matrix<F> L;
	private Matrix<F> U;
	private boolean isNonSingular;
	private Matrix<F> LU;


	/* ------------------------
	   Constructor
	 * ------------------------ */


	public static <R extends FieldElement<R>> LuDecomposition<R> decompose(Matrix<R> LU){

		R ZERO = LU.getField().zero();
		R ONE = LU.getField().one();

		if (!(ONE instanceof Comparable)){
			throw new IllegalArgumentException ("Field is not ordable");
		}

		LuDecomposition<R> d = new LuDecomposition<R>();

		DenseMatrix<R> L = new DenseMatrix<R>(LU.columnsCount(),LU.columnsCount(),ZERO);
		DenseMatrix<R> U = new DenseMatrix<R>(LU.columnsCount(),LU.columnsCount(),ZERO);

		int rows = LU.columnsCount();

		for (int r=0;r<rows;r++){

			L.set(r,r, ONE);

			for(int j=r;j < rows;j++) {
				R sum = ZERO;

				for(int s=0;s <= r-1;s++) {
					sum = sum.plus(L.get(r,s).times(U.get(s,j)));
				}
				U.set(r,j, LU.get(r,j).minus(sum));
			}

			for(int i = r + 1; i < rows;i++) {
				R sum = ZERO;

				for(int s= 0 ;s <= r- 1;s++) {
					sum = sum.plus (L.get(i,s).times(U.get(s,r)));
				}

				L.set(i,r,LU.get(i,r).minus(sum).over(U.get(r,r)));
			}
		}
		
		boolean isNonSingular = true; 
		
		for (int j = 0; j < LU.columnsCount(); j++) {
			if (LU.get(j, j).equals(ZERO)){
				isNonSingular = false;
				break;
			}
		}
	
		d.LU = LU;
		d.L = L;
		d.U = U;
		d.isNonSingular = isNonSingular;
		
		return d;

	}


	private LuDecomposition () {

	}


	/* ------------------------
	   Public Methods
	 * ------------------------ */

	/** Is the matrix nonsingular?
	   @return     true if U, and hence A, is nonsingular.
	 */

	public boolean isNonsingular () {
		return isNonSingular;
	}

	/** Return lower triangular factor
	   @return     L
	 */

	public Matrix<F> getL () {
		return L;
	}

	/** Return upper triangular factor
	   @return     U
	 */

	public Matrix<F> getU () {
		return U;
	}

	//	/** Return pivot permutation vector
	//	   @return     piv
	//	 */
	//
	//	public int[] getPivot () {
	//		int[] p = new int[piv.length];
	//		for (int i = 0; i <piv.length; i++) {
	//			p[i] = piv[i];
	//		}
	//		return p;
	//	}


	/** Solve A*X = B
	   @param  B   a Matrix with as many rows as A and any number of columns.
	   @return     X so that L*U*X = B(piv,:)
	   @exception  IllegalArgumentException Matrix row dimensions must agree.
	   @exception  RuntimeException  Matrix is singular.
	 */

//	public Matrix<F> solve (Matrix<F> B) {
//		if (B.rowsCount() != this.LU.rowsCount()) {
//			throw new IllegalArgumentException("Matrix row dimensions must agree.");
//		}
//		if (!this.isNonsingular()) {
//			throw new RuntimeException("Matrix is singular.");
//		}
//
//		// Copy right hand side with pivoting
//		int nx = B.columnsCount();
//		DenseMatrix<F> X = new DenseMatrix<F>(B , piv,0,nx-1 );
//
//		// Solve L*Y = B(piv,:)
//		for (int k = 0; k < LU.columnsCount(); k++) {
//			for (int i = k+1; i < LU.columnsCount(); i++) {
//				for (int j = 0; j < nx; j++) {
//					X.set(i,j, X.get(i, j).minus(X.get(k,j).times(LU.get(i,k))));
//				}
//			}
//		}
//		// Solve U*X = Y;
//		for (int k = LU.columnsCount()-1; k >= 0; k--) {
//			for (int j = 0; j < nx; j++) {
//				X.set(k,j, X.get(k, j).over(LU.get(k,k)));
//			}
//			for (int i = 0; i < k; i++) {
//				for (int j = 0; j < nx; j++) {
//					X.set(i,j, X.get(i, j).minus(X.get(k,j).times(LU.get(i,k))));
//				}
//			}
//		}
//		return X;
//	}
}


