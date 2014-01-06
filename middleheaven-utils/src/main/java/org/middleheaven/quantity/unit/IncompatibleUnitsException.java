package org.middleheaven.quantity.unit;



/**
 * Throwned when arithmetic sum operations are tried upon 
 * quantities with different units.
 * 
 * @author Sergio M.M. Taborda
 *
 */
public class IncompatibleUnitsException extends ArithmeticException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IncompatibleUnitsException(Unit a,Unit b){
		super("Units " + a + " and " + b + " are not compatible");
	}

}
