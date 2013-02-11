package org.middleheaven.quantity.math.impl;

import org.middleheaven.quantity.math.BigInt;
import org.middleheaven.quantity.math.Complex;
import org.middleheaven.quantity.math.Numeral;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.structure.MathStructuresFactory;

public class StandardMathStructuresFactory extends MathStructuresFactory {


	@Override
	public <T extends Numeral<?>> T numberFor(Class<T> superclass , Object ... values) {
		if (values.length==1){
			if (superclass.equals(Complex.class)){
				return superclass.cast(new RealPairComplex(values[0].toString()));
			} else if (superclass.equals(Real.class)){
				if (values[0] instanceof BigDecimalReal){
					return superclass.cast(values[0]);
				} else {
					return superclass.cast(BigDecimalReal.valueOf(values[0].toString()));
				}
			} else if (superclass.equals(BigInt.class)){
				return superclass.cast(new LongInteger(values[0].toString()));
			} else {
				throw new IllegalArgumentException(superclass.getName() + " is not a recognized Number");
			}
		} else if (values.length==2){
			if (superclass.equals(Complex.class)){
				return superclass.cast(new RealPairComplex((Real)values[0], (Real)values[1]));
			} else if (superclass.equals(Real.class)){
				
				Real a = numberFor(Real.class, values[0]);
				Real b = numberFor(Real.class, values[1]);
				
				return superclass.cast(a.over(b));
			} 
		} 
		throw new IllegalArgumentException("Array bigger than 1 is not supported for type " + superclass.getName());
	
	}



}
