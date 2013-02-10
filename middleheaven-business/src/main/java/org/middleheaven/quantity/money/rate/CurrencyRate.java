package org.middleheaven.quantity.money.rate;

import org.middleheaven.quantity.money.Currency;
import org.middleheaven.quantity.money.CentsMoney;
import org.middleheaven.quantity.time.DateTimeHolder;

public interface CurrencyRate {

	public Currency getOriginalCurrency();

	public Currency getTargetCurency();

	public DateTimeHolder getTimeStamp();

	/**
	 * 
	 * @return <code>true</code> if the rate is associated to a time frame. <code>false</code> otherwise.
	 */
	public boolean isTimeStamped();
	
	public CentsMoney convert(CentsMoney other);
}
