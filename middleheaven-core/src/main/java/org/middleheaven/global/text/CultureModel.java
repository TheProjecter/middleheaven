/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.global.text;

import java.text.Format;

import org.middleheaven.culture.Culture;
import org.middleheaven.global.text.writeout.NumberWriteoutFormatter;

/**
 * Represents an application specific Culture set of formats.
 * Localizable resources can belong in different locales. In particular they can be 
 * submited to a unique locale. That choice is application specific
 *  
 *
 */
public interface CultureModel {


	/**
	 * 
	 * @return
	 */
	public Format getDateFormat();

	public Format getNumberFormat();

	/**
	 * The culture specific {@link NumberWriteoutFormatter}.
	 * @return The culture specific {@link NumberWriteoutFormatter}
	 */
	public NumberWriteoutFormatter getNumberWriteoutFormat();


	/**
	 * Returns a {@link TimepointFormatter} for the given {@link Culture}
	 * 
	 * @return a <code>TimepointFormatter</code> for the given {@link Culture}
	 */
	public TimepointFormatter getTimestampFormatter ();

	/**
	 * Returns an approriated {@link QuantityFormatter}.
	 * 
	 * 
	 * @return an approriated {@link QuantityFormatter}.
	 */
	public QuantityFormatter getQuantityFormatter();

}
