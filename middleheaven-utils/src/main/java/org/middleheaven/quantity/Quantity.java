package org.middleheaven.quantity;

import org.middleheaven.quantity.unit.Measurable;
import org.middleheaven.quantity.unit.Unit;




/**
 * Base interface for the Quantity Pattern implementation
 * 
 * @author Sergio M.M. Taborda
 *
 */
public interface Quantity<E extends Measurable>{

	/**
	 * 
	 * @return this quantity unit
	 */
	public Unit<E> unit();

}
