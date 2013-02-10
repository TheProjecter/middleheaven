/**
 * 
 */
package org.middleheaven.domain.store;


/**
 * An action to be performed on a DataStorage with a given object.
 */
public abstract class StoreAction {

	private final EntityInstance object;
	private final StoreActionType type;

	public StoreAction (EntityInstance object, StoreActionType type){
		this.object = object;
		this.type = type;
	}

	protected EntityInstance getStorable(){
		return object;
	}
	
	public StoreActionType getStoreActionType(){
		return type;
	}
	
	/**
	 * Determines if an action is contrary to another. Example, saving and deleting the same instance 
	 * @return
	 */
	public boolean isContrary(StoreAction other){
		
		return (this.object.getIdentity() != null && this.object.getEntityModel().equals(other.object.getEntityModel()) && this.object.getIdentity().equals(other.object.getIdentity())) && this.type.isContraryTo(other.type);
	}
	
	public boolean isRepeated(StoreAction other){
		return (this.object.getIdentity() != null && this.object.getEntityModel().equals(other.object.getEntityModel()) && this.object.getIdentity().equals(other.object.getIdentity())) && this.type == other.type;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		return (other instanceof StoreAction) && equalsStore((StoreAction) other);
	}

	/**
	 * @param other
	 * @return
	 */
	private boolean equalsStore(StoreAction other) {
		return this.type.equals(other.type) && this.object == other.object;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.object.getIdentity() == null ? 0 : this.object.getIdentity().hashCode();
	}

	public String toString(){
		return type + "#" + this.object.getIdentity();
	}

}