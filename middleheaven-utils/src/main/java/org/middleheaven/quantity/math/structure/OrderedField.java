package org.middleheaven.quantity.math.structure;

import java.util.Comparator;

public interface OrderedField<F extends OrderedFieldElement<F>> extends Field<F>{

	
	public Comparator<F> getComparator();
}
