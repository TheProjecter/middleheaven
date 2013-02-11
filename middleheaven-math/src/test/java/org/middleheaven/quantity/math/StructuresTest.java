package org.middleheaven.quantity.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.quantity.math.vectorspace.DenseVectorSpaceProvider;
import org.middleheaven.quantity.math.vectorspace.LUDecomposition;
import org.middleheaven.quantity.math.vectorspace.Matrix;
import org.middleheaven.quantity.math.vectorspace.Vector;
import org.middleheaven.quantity.math.vectorspace.VectorSpace;
import org.middleheaven.quantity.math.vectorspace.VectorSpaceProvider;

public class StructuresTest {

	VectorSpaceProvider provider = DenseVectorSpaceProvider.getInstance();
	 
	@Test
	public void testNumbers(){

		Real a = Real.valueOf(1.2);
		Real b = Real.valueOf(1.2);
		Real c = Real.valueOf(2.4);

		assertEquals(c, a.plus(b));

		BigInt i = BigInt.valueOf(10);
		BigInt j = BigInt.valueOf(12);

		assertEquals(Real.valueOf(11.2), a.plus(i));
		assertEquals(j, i.times(a));
	}
	
	
	@Test
	public void matrixRemove(){
		VectorSpace<Vector<Real>, Real> space = provider.getVectorSpaceOver(RealField.getInstance(), 3);
		
		Matrix<Real> M = space.matrix(3,3, Real.valueOf(
				1 ,1 , 2, 
				1 , 2 , 1 , 
				2 ,1 ,1
		));

		M = M.remove(0, 0);
		
		assertEquals(2, M.rowsCount());
		assertEquals(2, M.columnsCount());
		
		System.out.println(M);
		
		assertEquals(Real.valueOf(2), M.get(0, 0));
		assertEquals(Real.valueOf(1), M.get(1, 0));
		assertEquals(Real.valueOf(1), M.get(0, 1));
		assertEquals(Real.valueOf(1), M.get(1, 1));
		
	}
	@Test
	public void matrix(){

		VectorSpace<Vector<Real>, Real> space = provider.getVectorSpaceOver(RealField.getInstance(), 3);
		
//		Vector<Real> v1 = space.vector(1,1,2);
//		Vector<Real> v2 = space.vector(1,2,1);
//		Vector<Real> v3 = space.vector(2,1,1);

		Matrix<Real> M = space.matrix(3,3, Real.valueOf(
				1 ,1 , 2, 
				1 , 2 , 1 , 
				2 ,1 ,1
		));

		Vector<Real> v4 = space.vector(2,2,4);
//		Vector<Real> v5 = space.vector(2,4,2);
//		Vector<Real> v6 = space.vector(4,2,2);

		Matrix<Real> N = space.matrix(3,3, Real.valueOf(2 , 2 , 4, 2 ,4 ,2,4, 2, 2));

		// determinant
		Real det = M.determinant();

		assertEquals(Real.valueOf(-4),det);

		assertEquals(Real.valueOf(4),M.trace());

		// transpose 
		assertEquals(M , M.transpose());

		// multiplication
		assertEquals(N , M.times(Real.valueOf(2.0)));

		// addition
		assertEquals(N , M.plus(M));

		// vector x matrix
		Vector<Real> v7 = space.vector(12,10,10);
		assertEquals(v7, M.rightTimes(v4));

		// Matrix equality
		Matrix<Real> P = space.matrix(3,3, Real.valueOf(1 , 1 , 2, 1 ,2 ,1, 2, 1, 1));
		assertEquals(M, P);

		// Matrix multiplication
		Matrix<Real> Q = space.matrix(3,3, Real.valueOf(12 , 10 , 10, 10 ,12 ,10, 10, 10, 12));
		assertEquals(Q, M.times(N));

		// Adjoint
		Matrix<Real> A = space.matrix(3,3, Real.valueOf(1 , 1 , -3, 1 ,-3 ,1, -3, 1, 1));
		assertEquals(A, M.adjoint());

		// Identity Multiplication
		Matrix<Real> I = space.identity(3);
		assertEquals(I, I.times(I));

		assertEquals(I.getRow(1), I.getColumn(1));

		Matrix<Real> X = space.matrix(3,3, 
				Real.valueOf(
						1 ,  3 , 3, 
						1 , 4 ,3, 
						1, 3, 4
				));

		// Identity relation M  = M.I and M = I.M
		assertEquals(X, X.times(I));
		assertEquals(X, I.times(X));

		Matrix<Real> XInv = space.matrix(3,3, Real.valueOf(
				7 ,  -3 , -3, 
				-1 , 1 ,0, 
				-1, 0, 1
		));

		// inverse
		assertEquals(XInv, X.inverse());

		// Invertion relation I = M.M^-1
		assertEquals(I, X.times(X.inverse()));
		
		
		// transpose of the transpose is it self
		
		assertTrue ( M == M.transpose().transpose());

	}

	@Test(expected=ArithmeticException.class)
	public void matrixInversNotExists(){
		VectorSpace<Vector<Real>, Real> space = provider.getVectorSpaceOver(RealField.getInstance(), 3);
		
		Matrix<Real> X = space.matrix(3,3, Real.valueOf(
						1 ,  3 , 3, 
						1 , 3 ,3,  // two equal rows => zero determinant
						1, 3, 4
				));

		X.inverse();

	}

	@Test 
	public void randomMatrix (){
		VectorSpace<Vector<Real>, Real> space = provider.getVectorSpaceOver(RealField.getInstance(), 3);
		
		Matrix<Real> R = space.random(3,3,2);

		// same seed produces the same matrix
		assertEquals(R, space.random(3,3,2));

		// the matrix is invertible
		assertTrue(R.hasInverse());
		
		Matrix<Real> I = space.identity(3);
		
		assertEquals(I, R.times(R.inverse()));
		

	}


	@Test 
	public void matrixLU(){
		VectorSpace<Vector<Real>, Real> space = provider.getVectorSpaceOver(RealField.getInstance(), 3);
		
		//http://en.wikipedia.org/wiki/LU_decomposition
		Matrix<Real> A = space.matrix(2,2, Real.valueOf(
				4, 3,
				6, 3
		));

		Matrix<Real> U = space.matrix(2,2, Real.valueOf(
				4 , 3 ,
				0, -1.5
		));


		Matrix<Real> L = space.matrix(2,2, Real.valueOf(
				1, 0 ,
				1.5 , 1
		));

		LUDecomposition<Real> lud = LUDecomposition.decompose(A);

		
		assertEquals("Incorrect 2x2 L", L , lud.getL());
		assertEquals("Incorrect 2x2 U", U , lud.getU());
		
		assertEquals(A , L.times(U));
		assertEquals(A , lud.getL().times(lud.getU()));

		
		 A = space.matrix(3,3, Real.valueOf(
				6 , -2 , 0, 
				9 ,-1 ,1, 
				3, 7, 5
		));

		U = space.matrix(3,3, new Real []{
				Real.valueOf(6) , Real.valueOf( -2 ), Real.valueOf(0),
				Real.valueOf(0) , Real.valueOf(2 ), Real.valueOf(1 ),
				Real.valueOf(0) , Real.valueOf(0 ), Real.valueOf(1)	
		});

		L = space.matrix(3,3, new Real []{
				Real.valueOf(1) , Real.valueOf(0), Real.valueOf(0),
				Real.valueOf(3,2 ) , Real.valueOf(1), Real.valueOf(0),
				Real.valueOf(1, 2) , Real.valueOf(4), Real.valueOf(1)	
		});
		
		
		
		assertEquals("wrong test data" , A , L.times(U));
		
		lud = LUDecomposition.decompose(A);

		assertEquals("Incorrect decomposition product" ,A , lud.getL().times(lud.getU()));
		
		assertEquals("Incorrect 3x3 L", L , lud.getL());
		assertEquals("Incorrect 3x3 U", U , lud.getU());
		
	
	
		
		
		Matrix<Real> N = space.matrix(3,3, Real.valueOf(
				1 , 1 , 2, 
				1 ,2 ,1,
				2, 1, 1
		));

		lud = LUDecomposition.decompose(N);

		assertEquals(N , lud.getL().times(lud.getU()));
	}


}
