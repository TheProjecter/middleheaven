package org.middleheaven.global.text;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

import org.middleheaven.global.Culture;
import org.middleheaven.quantity.Quantity;
import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.measure.DecimalMeasure;
import org.middleheaven.quantity.money.CentsMoney;

public class QuantityFormatter implements Formatter<Quantity<?>>{

	private Culture culture;

	public QuantityFormatter(Culture culture) {
		this.culture = culture;
	}

	@Override
	public String format(Quantity<?> object) {
		if (object instanceof CentsMoney){
			return formatMoney((CentsMoney)object);
		} else if (object instanceof org.middleheaven.quantity.math.BigInt ){
			return formatInteger((org.middleheaven.quantity.math.BigInt)object);
		} else if (object instanceof Real ){
			return formatReal((Real)object);
		} else if (object instanceof DecimalMeasure ){
			return formatMeasure((DecimalMeasure)object);
		} else {
			return object.toString();
		}
	}

	private String formatMeasure(DecimalMeasure object) {
		String value = formatReal((Real) object.amount());
		String error = formatReal((Real) object.uncertainty());
		
		return value + '\u00B1' + error + " " + object.unit().symbol();
	}

	private String formatReal(Real object) {
		NumberFormat format = NumberFormat.getNumberInstance(culture.toLocale());
		BigDecimal decimal = object.asNumber();
		format.setMaximumFractionDigits(Math.abs(decimal.scale()));
		return format.format(object.asNumber());
	}

	private String formatInteger(org.middleheaven.quantity.math.BigInt object) {
		NumberFormat format = NumberFormat.getIntegerInstance(culture.toLocale());
		
		return format.format(object.asNumber());
	}

	private String formatMoney(CentsMoney object) {
		NumberFormat format = NumberFormat.getCurrencyInstance(culture.toLocale());
		Currency currency = Currency.getInstance(object.currency().toString());
		format.setCurrency(currency);
		return format.format(object.amount().asNumber());
	}



}
