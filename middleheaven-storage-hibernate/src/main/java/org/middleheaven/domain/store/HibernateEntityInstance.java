/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.identity.Identity;

/**
 * 
 */
public class HibernateEntityInstance implements EntityInstance {

	
	private Object object;

	public HibernateEntityInstance(Object object){
		this.object = object;
	}
	
	public Object getObject(){
		return object;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public StorableState getStorableState() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStorableState(StorableState state) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity getIdentity() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIdentity(Identity id) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityModel getEntityModel() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityInstanceField getField(String name) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<EntityInstanceField> getFields() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void copyFrom(EntityInstance instance) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

}
