package org.middleheaven.quantity.money.rate;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.money.Currency;
import org.middleheaven.quantity.money.Money;
import org.middleheaven.quantity.time.DateTimeHolder;

public class IdentityCurrencyRate implements CurrencyRate {

	private Currency currency;
	
	public IdentityCurrencyRate(Currency currency) {
		super();
		this.currency = currency;
	}

	@Override
	public Money convert(Money other){
		return other.times(Real.valueOf(1));
	}

	@Override
	public Currency getOriginalCurrency() {
		return currency;
	}

	@Override
	public Currency getTargetCurency() {
		return currency;
	}

	@Override
	public DateTimeHolder getTimeStamp() {
		return null;
	}

	@Override
	public boolean isTimeStamped() {
		return false;
	}
	
	public boolean equals(Object other){
		return other instanceof IdentityCurrencyRate &&
		((IdentityCurrencyRate)other).currency.equals(this.currency);
	}
	
	public int hashCode(){
		return currency.hashCode();
	}

}
