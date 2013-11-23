package org.middleheaven.domain.model;

import org.middleheaven.model.ModelSet;
import org.middleheaven.util.Maybe;

/**
 * Holds the individual {@code EntityModel}s for all entities in the domain.
 * 
 */
public interface DomainModel extends ModelSet<EntityModel>{


	public <E> Maybe<EnumModel> getEmumModel(Class<E> type);
	
}
