package org.middleheaven.domain.store;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.util.identity.Identity;

/**
 * Abstraction of an entity instance.
 */
public interface EntityInstance  {
	
	/**
	 * 
	 * @return the instance state.
	 */
	public StorableState getStorableState();

	/**
	 * 
	 * @param state the new state for this instance
	 */
	public void setStorableState(StorableState state);

	/**
	 * 
	 * @return the identity of this instance
	 */
	public Identity getIdentity();

	/**
	 * 
	 * @param id the identity for this instance
	 */
	public void setIdentity(Identity id);

	/**
	 * The entity's model.
	 * @return the model.
	 */
	public EntityModel getEntityModel();
	
	/**
	 * Retrieve the field with the given name.
	 * @param name
	 * @return the entity field.
	 */
	public EntityInstanceField getField(String name);
	
	/**
	 * All fields.
	 * @return all this entity fields.
	 */
	public Enumerable<EntityInstanceField> getFields();
	
	
	/**
	 * Copy fields from the given instance.
	 * @param instance
	 */
	public void copyFrom(EntityInstance instance);

	
}
