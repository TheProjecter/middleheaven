/**
 * 
 */
package org.middleheaven.global.text;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;

import org.middleheaven.culture.Culture;
import org.middleheaven.global.text.writeout.NumberWriteoutFormatter;

/**
 * 
 */
public class StandardCultureModel implements CultureModel {

	private Culture culture;
	
	public StandardCultureModel (Culture culture){
		this.culture = culture;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Format getDateFormat() {
		return DateFormat.getDateInstance(DateFormat.SHORT, culture.toLocale());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Format getNumberFormat() {
		return NumberFormat.getInstance(culture.toLocale());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NumberWriteoutFormatter getNumberWriteoutFormat() {
		throw new UnsupportedOperationException("Not implememented yet");
	}
	
	/**
	 * 
	 * @param culture
	 * @return
	 */
	@Override
	public QuantityFormatter getQuantityFormatter() {
		return new QuantityFormatter(culture);
	}

	/**
	 * 
	 * @param culture
	 * @return
	 */
	@Override
	public TimepointFormatter getTimestampFormatter() {
		return new TimepointFormatter(culture);
	}

}
