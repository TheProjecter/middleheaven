/**
 * 
 */
package org.middleheaven.domain.store;

import java.util.HashSet;
import java.util.Set;

import org.middleheaven.collections.enumerable.Enumerables;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Mapper;
import org.middleheaven.util.identity.Identity;

/**
 * 
 */
public abstract class AbstractDomainStoreManager implements DomainStoreManager {


	/**
	 * 
	 */
	private static final class DomainStoreCommiter implements
			StoreActionCommiter {
		
		private AbstractDomainStoreManager abstractDomainStoreManager;

		/**
		 * Constructor.
		 * @param abstractDomainStoreManager
		 */
		public DomainStoreCommiter(
				AbstractDomainStoreManager abstractDomainStoreManager) {
			this.abstractDomainStoreManager = abstractDomainStoreManager;
		}

		@Override
		public void commit(StoreAction action) {
			EntityInstance instance = action.getStorable();
			
			switch (action.getStoreActionType()){
			case DELETE:
				
				abstractDomainStoreManager.deleteInstance(instance);
		
				instance.setStorableState(StorableState.DELETED);
				break;
			case INSERT:
				
				abstractDomainStoreManager.insertInstance(instance);

				instance.setStorableState(StorableState.RETRIVED);
				break;
			case UPDATE:
				
				abstractDomainStoreManager.updateInstance(instance);
				
			
				instance.setStorableState(StorableState.RETRIVED);
				break;
			 default:
				throw new UnsupportedOperationException(action.getStoreActionType().name() + " is not suported StoreAction ");
			}
		}
	}

	private DomainModel domainModel;

	protected AbstractDomainStoreManager (DomainModel domainModel){
		this.domainModel = domainModel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void commit(StorageUnit unit) {
		unit.commitTo(new DomainStoreCommiter(this));
	}

	protected abstract void deleteInstance(EntityInstance instance);
	protected abstract void insertInstance(EntityInstance instance);
	protected abstract void updateInstance(EntityInstance instance);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void roolback(StorageUnit unit) {
		unit.roolback();
	}

	@Override
	public final DomainModel getDomainModel() {
		return domainModel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Identity getIdentityFor(Object object) {
		if (object instanceof EntityInstance) {
			return ((EntityInstance) object).getIdentity();
		}
		return null;
	}
	

	protected StoreAction assignAction(EntityInstance p){
		switch (p.getStorableState()) {
		case DELETED:
			if (p.getIdentity() == null) {
				return null;
			}
			return new DeleteAction(p);
		case EDITED:
			// update
			return new UpdateAction(p);
		case FILLED:
			if (!p.getEntityModel().isIdentityAssigned()){
				if (p.getIdentity() == null) {
					assignIdentity(p);
					p.setStorableState(p.getStorableState().edit());
				} 
			}
			return new InsertAction(p);
		case NOT_PERSISTED:
			if (!p.getEntityModel().isIdentityAssigned()){
				if (p.getIdentity() == null) {
					assignIdentity(p);
					p.setStorableState(p.getStorableState().edit());
				} else {
					throw new IllegalStateException("Key is already set for a non assigned identity entity");
				}
			}
			return new InsertAction(p);
		case RETRIVED:
			// no-op
			return null;
		default:
			throw new IllegalStateException(p.getStorableState() + " is unkown");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> void remove(T obj, StorageUnit unit) {
		EntityInstance p = this.merge(obj);
		if (p.getIdentity()!=null) {
			p.setStorableState(StorableState.DELETED);
			unit.addAction(assignAction(p));
		}
		// else
		// not identified
		// do nothing as this object is not in the store

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T store(T obj, StorageUnit unit) {

		if (obj == null){
			throw new IllegalArgumentException("Cannot store null");
		}
		EntityInstance p = this.merge(obj);
		final Set<EntityInstance> all = new HashSet<EntityInstance>();
		
		flatten(p,all);


		Enumerables.asEnumerable(all).map(new Mapper<StoreAction, EntityInstance>(){

			@Override
			public StoreAction apply(EntityInstance s) {
				return AbstractDomainStoreManager.this.assignAction(s);
			}

		}).forEach(new UnitAddBlock(unit));

		return (T) obj.getClass().cast(p);
	}
	
	protected abstract EntityInstance merge(Object obj);

	protected abstract void assignIdentity(EntityInstance p);
	
	protected abstract void flatten(EntityInstance p, Set<EntityInstance> all);
	
	private static class UnitAddBlock implements Block<StoreAction> {
		
		StorageUnit unit;
		public UnitAddBlock (StorageUnit unit){
			this.unit = unit;
		}
		@Override
		public void apply(StoreAction action) {
			unit.addAction(action);
		}
	}
}
