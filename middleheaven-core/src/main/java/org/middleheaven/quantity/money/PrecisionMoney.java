/**
 * 
 */
package org.middleheaven.quantity.money;

import org.middleheaven.quantity.math.Real;


/**
 * 
 */
public class PrecisionMoney extends Money {

	
	private static final long serialVersionUID = -3041804631392543589L;

	Real amount;
	
	PrecisionMoney (Real amount, Currency currency){
		super(currency);
		this.amount = amount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Money negate() {
		return new PrecisionMoney(this.amount.negate(), currency);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isZero() {
		return amount.isZero();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Real amount() {
		return amount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Money times(int multiplier) {
		return new PrecisionMoney(amount.times(multiplier), currency);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Money times(Real other) {
		return new PrecisionMoney(this.amount.times(other), currency);
	}

	public String toString(){
		return amount().toString() + " " + currency.toString();
	}
	
	public int hashCode(){
		return amount.hashCode();
	}
	


	

}
