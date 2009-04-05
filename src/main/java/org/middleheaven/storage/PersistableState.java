package org.middleheaven.storage;

/**
 * Storable persistance states
 */
public enum PersistableState {

	/**
	 * The object has just been created. It is not persisted
	 * and has no data
	 */
	BLANK,
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

	public PersistableState edit(){
		if (this==BLANK){
			return FILLED;
		} else if(this==RETRIVED) {
			return EDITED;
		} 
		// any other state, mantain it
		return this;
	}

	public PersistableState delete(){
		switch (this){
		case BLANK:
		case ERASED:	
			return this; // it self
		case FILLED:
			return BLANK;
		case DELETED:
			return ERASED;
		default:
			return DELETED;
		}
	}
}
