package org.middleheaven.quantity.unit;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a set of units 
 * 
 * @author Sergio M.M. Taborda
 *
 */
public abstract class UnitSystem {

	protected static final Map<String , Unit<?>> ALL_UNITS = new HashMap<String , Unit<?>>();
	

	/**
	 * Obtains the correct unit, in this system, for a given measurable.
	 * @param <E> measurable type
	 * @param measurable 
	 * @return the correct unit, in this system,for a given measurable.
	 */
	@SuppressWarnings("unchecked")
	public final <E extends Measurable> Unit<E> getMeasuableUnit(Class<E> measurable) {
		return (Unit<E>) ALL_UNITS.get(measurable.getName());
	}
	/**
	 * 
	 * @return A set of all units endorsed by this units system.
	 */
	public final Collection<Unit<?>> units() {
		return Collections.unmodifiableCollection(ALL_UNITS.values());
	}
}
