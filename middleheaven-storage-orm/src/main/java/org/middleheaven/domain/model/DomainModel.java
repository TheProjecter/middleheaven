package org.middleheaven.domain.model;

import org.middleheaven.model.ModelSet;
import org.middleheaven.util.function.Maybe;

/**
 * Holds the individual {@code EntityModel}s for all entities in the domain.
 * 
 */
public interface DomainModel extends ModelSet<EntityModel>{


	public <E extends Enum> Maybe<EnumModel> getEmumModel(Class<E> type);
	
}
