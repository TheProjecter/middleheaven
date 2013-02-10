/**
 * 
 */
package org.middleheaven.quantity.money;

import java.io.Serializable;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.RealField;
import org.middleheaven.quantity.math.structure.GroupAdditive;
import org.middleheaven.quantity.measure.Amount;
import org.middleheaven.quantity.unit.Unit;

/**
 * 
 */
abstract class Money implements Serializable, Amount<Money, org.middleheaven.quantity.measurables.Currency>, Comparable<Money>{

	private static final long serialVersionUID = -6885304390183764842L;
	
	protected Currency currency;

	protected Money(Currency currency){
		this.currency = currency;
	}
	
	public Unit<org.middleheaven.quantity.measurables.Currency> unit(){
		return currency;
	}

	public Currency currency(){
		return currency;
	}

	public Money plus(Money other) {
		return MoneyUtils.plus(this, other);
	}
	
	public Money minus(Money other) {
		return MoneyUtils.plus(this, other.negate());
	}
	
	protected abstract Real amount();

	public abstract Money times(Real other);
	
	@Override
	public final int compareTo(Money other) {
		if (!this.currency.equals(other.currency)){
			throw new IllegalArgumentException("Cannot compare money in different currencies.");
		}
		return  this.amount().compareTo(other.amount());
	}

	public boolean greaterThan(Money other) {
		return (compareTo(other) > 0);
	}

	public boolean lesserThan(Money other) {
		return (compareTo(other) < 0);
	}

	public boolean greaterThanOrEqual(Money other) {
		return (compareTo(other) >= 0);
	}

	public boolean lesserThanOrEqual(Money other) {
		return (compareTo(other) <= 0);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupAdditive<Money> getAlgebricStructure() {
		return new GroupAdditive<Money>(){

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
			public Money zero() {
				return new CentsMoney(RealField.getInstance().zero(), currency);
			}
			
		};
	}

	/**
	 * @param multiplier
	 * @return
	 */
	public abstract Money times(int multiplier);
	
	public final boolean equals(Object other){
		return (other instanceof Money) && equalsMoney((Money) other);
	}
	
	public abstract int hashCode();

	/**
	 * @param other
	 * @return
	 */
	protected boolean equalsMoney(Money other){
		return this.currency.equals(other.currency) && this.amount().equals(other.amount());
	}
}
