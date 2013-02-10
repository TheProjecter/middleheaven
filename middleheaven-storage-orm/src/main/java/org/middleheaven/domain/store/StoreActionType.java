/**
 * 
 */
package org.middleheaven.domain.store;

/**
 * 
 */
public enum StoreActionType {

	
	INSERT,
	UPDATE,
	DELETE;

	/**
	 * @param type
	 * @return
	 */
	public boolean isContraryTo(StoreActionType other) {
		return (this == DELETE && ( other == INSERT || other == UPDATE )) || (other == DELETE && ( this == INSERT || this == UPDATE));
	}

	/**
	 * @return
	 */
	public boolean isDelete() {
		return this == DELETE;
	}

	/**
	 * @return
	 */
	public boolean isInsert() {
		return this == INSERT;
	}
	
	/**
	 * @return
	 */
	public boolean isUpdate() {
		return this == UPDATE;
	}
}
