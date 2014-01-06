package org.middleheaven.quantity.money;

import java.util.Locale;

import org.middleheaven.quantity.unit.CompositeUnit;
import org.middleheaven.quantity.unit.Dimension;
import org.middleheaven.quantity.unit.Unit;

/**
 * 
 */
public abstract class Currency extends Unit<org.middleheaven.quantity.measurables.Currency>{

	public static Currency currency(java.util.Currency currency ){
		return new ISOCurrency(currency);
	}
	
	public static Currency currency(String isoCode ){
		try {
			return new ISOCurrency(java.util.Currency.getInstance(isoCode));
		} catch (IllegalArgumentException e){
			throw new IllegalArgumentException(isoCode + " is not a valid ISO currency code");
		}
		
	}
	
	public static Currency currency(Locale locale ){
		return new ISOCurrency(java.util.Currency.getInstance(locale));
	}


	public abstract int getDefaultFractionDigits();
	
	@Override
	public Dimension<org.middleheaven.quantity.measurables.Currency> dimension() {
		return Dimension.CURRENCY;
	}

	@Override
	public boolean equalsOther(Unit<?> other) {
		return  (other instanceof Currency) && other.dimension().equals(Dimension.CURRENCY) && equalsOther((Currency)other);
	}
	
	private boolean equalsOther(Currency other) {
		return other.symbol().equals(this.symbol());
	}
	
	@Override
	public boolean isCompatible(Unit<?> other) {
		return this.equalsOther(other);
	}
	

	@Override
	public Unit<org.middleheaven.quantity.measurables.Currency> raise(int value) {
		return CompositeUnit.raise (this, value);
	}

}
