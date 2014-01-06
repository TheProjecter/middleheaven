package org.middleheaven.quantity.convertion;

import org.middleheaven.quantity.unit.Measurable;
import org.middleheaven.quantity.unit.Unit;

public interface Convertable<E extends Measurable,  T> {

	public T convertTo(Unit<E> newUnit);
	public Unit<E> unit();	
}
