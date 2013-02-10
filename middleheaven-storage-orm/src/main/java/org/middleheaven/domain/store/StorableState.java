package org.middleheaven.domain.store;

/**
 * Storable persistance states
 */
public enum StorableState {

	/**
	 * The object has just been created. It is not persisted
	 * and has no data
	 */
	NOT_PERSISTED,
	/**
	 * The object its not persisted and has data on it
	 */
	FILLED,
	/**
	 * The object has data retrieved from persistence
	 */
	RETRIVED,
	/**
	 * The object has been persisted but its data is different from the data persisted
	 */
	EDITED,
	/**
	 * The object has been persisted before, but is <i>logically</i> removed from persistence.
	 * Queries do not show this object unless explicitly told so.
	 * 
	 */
	DELETED,
	/**
	 * The object is no longer persisted. Information regarding it was been erased from the persistence store
	 */
	ERASED
	;

	/**
	 * 
	 * @return {@code true} if this state does not cause any change on the stored data.
	 */
	public boolean isNeutral(){
		return this == NOT_PERSISTED || this == RETRIVED;
	}
	
	public StorableState edit(){
		if (this==NOT_PERSISTED){
			return FILLED;
		} else if(this==RETRIVED) {
			return EDITED;
		} 
		// any other state, mantain it
		return this;
	}

	public StorableState delete(){
		switch (this){
		case NOT_PERSISTED:
		case ERASED:	
			return this; // it self
		case FILLED:
			return NOT_PERSISTED;
		case DELETED:
			return ERASED;
		default:
			return DELETED;
		}
	}


}
