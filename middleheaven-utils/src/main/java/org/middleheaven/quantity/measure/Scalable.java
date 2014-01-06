package org.middleheaven.quantity.measure;

import org.middleheaven.quantity.math.Real;
import org.middleheaven.quantity.math.structure.GroupAdditiveElement;
import org.middleheaven.quantity.unit.Measurable;
import org.middleheaven.quantity.unit.Unit;

public interface Scalable<E extends Measurable,T extends Scalable<E,T>> extends GroupAdditiveElement<T> {

	public T over(Real other, Unit<E> unit);
	public T times(Real other, Unit<E> unit);
	public Unit<E> unit();
	public T one();
}