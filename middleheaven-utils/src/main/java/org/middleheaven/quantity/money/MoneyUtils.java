/**
 * 
 */
package org.middleheaven.quantity.money;

import org.middleheaven.quantity.unit.IncompatibleUnitsException;


/**
 * 
 */
class MoneyUtils {

	/**
	 * @param a
	 * @param b
	 * @return
	 */
	static Money plus (Money a , Money b){
		assertCompatible(a.currency, b.currency);
		
		if (a instanceof CentsMoney){
			if (b instanceof CentsMoney){
				CentsMoney ca = (CentsMoney)a;
				CentsMoney cb = (CentsMoney)b;
				
				return new CentsMoney(ca.amount + cb.amount, ca.currency);
				
			} else {
				return plus ((CentsMoney) a , (PrecisionMoney) b );
			}
			
		} else {
			if (b instanceof PrecisionMoney){
				PrecisionMoney pa = (PrecisionMoney)a;
				PrecisionMoney pb = (PrecisionMoney)b;
				
				return new PrecisionMoney(pa.amount.plus(pb.amount) , pa.currency);
				
			} else {
				return plus ((CentsMoney) b , (PrecisionMoney) a );
			}
			
		}
	}
	
	private static Money plus(CentsMoney a, PrecisionMoney b) {
		return new PrecisionMoney(a.amount().plus(b.amount) , a.currency);
	}

	private static void assertCompatible(Currency a, Currency b) throws IncompatibleUnitsException {
		if (!a.isCompatible(b)){
			throw new IncompatibleUnitsException(a,b);
		}
	}
	
}
