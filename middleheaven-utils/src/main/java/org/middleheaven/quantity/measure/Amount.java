package org.middleheaven.quantity.measure;

import org.middleheaven.quantity.Quantity;
import org.middleheaven.quantity.math.structure.GroupAdditiveElement;
import org.middleheaven.quantity.unit.Measurable;

/**
 * An amount represents a addable number associated with a {@link Measure}.
 * 
 * @param <T> the subjacent {@link GroupAdditiveElement}.
 * @param <E> the subjacent {@link Measure}.
 */
public interface Amount<T extends GroupAdditiveElement<T>, E extends Measurable> extends Quantity<E>, GroupAdditiveElement<T> {

	
}
