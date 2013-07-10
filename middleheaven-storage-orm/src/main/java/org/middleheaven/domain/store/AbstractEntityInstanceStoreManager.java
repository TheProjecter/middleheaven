package org.middleheaven.domain.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.reflection.metaclass.MetaBean;
import org.middleheaven.core.reflection.metaclass.ReflectionBean;
import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.query.QueryExecuter;
import org.middleheaven.domain.query.QueryParametersBag;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.util.criteria.ReadStrategy;


/**
 * Common storable state controller.
 */
public abstract  class AbstractEntityInstanceStoreManager extends AbstractDomainStoreManager {

	private final EntityInstanceStorage storage;
	private final IdentityManager identityManager;
	private final EventListenersSet<DomainStoreListener> listeners = EventListenersSet.newSet(DomainStoreListener.class); 
	
	protected AbstractEntityInstanceStoreManager (IdentityManager identityManager, EntityInstanceStorage storage, DomainModel domainModel){
		super(domainModel);
		this.identityManager = identityManager;
		this.storage = storage;
		this.storage.setStorableStateManager(this);
	}
	
	protected void assignIdentity(EntityInstance storable){
		this.identityManager.assignIdentity(storable);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Query<T> createQuery(final EntityCriteria<T> criteria, final StorageUnit unit) {
		

		return new ParametrizedCriteriaQuery<T>(criteria, new QueryExecuter (){

			@Override
			public <E> Collection<E> retrive(EntityCriteria<E> query, ReadStrategy readStrategy,QueryParametersBag queryParametersBag) {
				return unit.<E>filter(storage.createQuery(query , readStrategy).fetchAll(), query);
			}

			@Override
			public <E> long count(EntityCriteria<E> query, QueryParametersBag queryParametersBag) {
				return storage.createQuery(criteria, ReadStrategy.defaultStrategy()).count();
			}

			@Override
			public <E> boolean existsAny(EntityCriteria<E> query, QueryParametersBag queryParametersBag) {
				return !storage.createQuery(criteria, ReadStrategy.defaultStrategy()).isEmpty();
			}
			
		});


	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addStorageListener(DomainStoreListener listener) {
		this.listeners.addListener(listener);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeStorageListener(DomainStoreListener listener) {
		this.listeners.removeListener(listener);
	}
	
	protected void fireAddEvent(Object instance) {

		listeners.broadcastEvent().onEntityAdded(new DomainChangeEvent(instance));

	}

	protected void fireRemovedEvent(Object instance) {
		
		listeners.broadcastEvent().onEntityRemoved(new DomainChangeEvent(instance));
		
	}

	protected void fireUpdatedEvent(Object instance) {
		listeners.broadcastEvent().onEntityChanged(new DomainChangeEvent(instance));
		
	}
	
	/**
	 * Wraps the object with a {@link EntityInstance} interface.
	 * If the object is already a {@link EntityInstance}, return the object as it is.
	 * 
	 * @param obj the object to merge.
	 * @return
	 */
	protected EntityInstance merge(Object obj){
	

		if ( obj == null){
			return null;
		} else if (obj instanceof EntityInstance){
			return (EntityInstance) obj;
		} else if (obj instanceof Class){
			throw new IllegalArgumentException("Cannot merge a Class object.");
		}else {

			// not managed yet
			MetaBeanEntityInstance p;
			
			if (obj instanceof MetaBean){
				EntityModel model = this.getDomainModel().getModelFor(((MetaBean) obj).getMetaClass().getName());
				p = new MetaBeanEntityInstance(model, (MetaBean) obj);
			} else {
				EntityModel model = this.getDomainModel().getModelFor(obj.getClass().getName());
				p = new MetaBeanEntityInstance(model, new ReflectionBean(obj));
			}
			Object merged = Introspector.of(obj.getClass()).newProxyInstance(new EntityManagerProxyHandler(p), EntityInstance.class);
			
			return (EntityInstance) obj.getClass().cast(merged);
		}
		
	}
	
	
	protected EntityInstance newEntityInstance(EntityModel model){
		return merge(model.getEntityClass().newInstance());
	}


	protected void flatten(EntityInstance p, Set<EntityInstance> all){

		if (all.contains(p)){ // loop
			return;
		}

		// add only if it will cause any change
		if (!p.getStorableState().isNeutral()){
			all.add(p);
		}

		for (EntityInstanceField field : p.getFields()) {
			EntityFieldModel fieldModel = field.getModel();
			if (!fieldModel.isTransient()){
				if(fieldModel.getDataType().isToOneReference()){
					EntityInstance merged = this.merge(field.getValue());
					if(merged != null){
						flatten(merged, all);
					}
				} else if (fieldModel.getDataType().isToManyReference()){
				
					@SuppressWarnings({ "unchecked", "rawtypes" })
					Collection<?> allRefereed = new ArrayList((Collection<?>) field.getValue());
					for (Object o : allRefereed){
						if(o != null){
							EntityInstance merged = this.merge(o);
							field.remove(o);
							field.add(merged);
							flatten(merged, all);
						}
					}
				}
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void deleteInstance(EntityInstance instance) {
		fireRemovedEvent(instance);
		storage.remove(Collections.singleton(instance));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void insertInstance(EntityInstance instance) {
		fireAddEvent(instance);
		storage.insert(Collections.singleton(instance));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateInstance(EntityInstance instance) {
		fireUpdatedEvent(instance);
		storage.update(Collections.singleton(instance));
	}


}
