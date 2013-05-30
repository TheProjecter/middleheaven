package org.middleheaven.quantity.money.rate;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.money.CentsMoney;
import org.middleheaven.quantity.money.Currency;
import org.middleheaven.quantity.time.DateTimeHolder;

public class RealCurrencyRate implements CurrencyRate{

	private final Currency originalCurrency;
	private final Currency targetCurency;
	private final Real rate;
	private final DateTimeHolder timeStamp;

	public RealCurrencyRate(Currency originalCurrency, Currency targetCurency, 
			Real rate, DateTimeHolder timeStamp) {

		this.originalCurrency = originalCurrency;
		this.targetCurency = targetCurency;
		this.rate = rate;
		this.timeStamp = timeStamp;
	}

	public Currency getOriginalCurrency() {
		return originalCurrency;
	}

	public Currency getTargetCurency() {
		return targetCurency;
	}

	public DateTimeHolder getTimeStamp() {
		return timeStamp;
	}


	public CentsMoney convert(CentsMoney other){
		if (!other.unit().equals(this.originalCurrency)){
			throw new IllegalArgumentException(
					"Currency rate for " + targetCurency + 
					"/" + originalCurrency + " cannot convert money in " 
					+ other.unit()
			);
		}
		
		return  CentsMoney.valueOf(other.amount().times(rate),this.targetCurency);
	}

	@Override
	public boolean isTimeStamped() {
		return timeStamp!=null;
	}

	public int hashCode(){
		return rate.hashCode() ^ this.targetCurency.hashCode();
	}
	
	public boolean equals(Object other){
		return other instanceof RealCurrencyRate && equals((RealCurrencyRate)other);
	}
	
	private boolean equals(RealCurrencyRate other){
		return other.rate.equals(this.rate) && 
		other.originalCurrency.equals(this.originalCurrency) && 
		other.targetCurency.equals(this.targetCurency) && 
		(other.timeStamp==null? this.timeStamp==null : other.timeStamp.equals(this.timeStamp));
	}
}
