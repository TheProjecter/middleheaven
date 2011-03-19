package org.middleheaven.model.domain.repository;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.domain.model.repository.Repository;
import org.middleheaven.domain.model.repository.RepositoryChangeEvent;
import org.middleheaven.domain.model.repository.RepositoryListener;
import org.middleheaven.model.domain.DomainModel;
import org.middleheaven.storage.ListQuery;
import org.middleheaven.storage.Query;

public abstract class AbstractRepository<E> implements Repository<E> {

	private Set<RepositoryListener> listeners = new CopyOnWriteArraySet<RepositoryListener> ();
	private DomainModel domainModel;

	public AbstractRepository(){
		
	}

	protected Class<E> getEntityClass () {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		@SuppressWarnings("unchecked") Class<E> type = (Class<E>) parameterizedType.getActualTypeArguments()[0];
		return type;
	}
	
	@Override
	public void addRepositoryListener(RepositoryListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeRepositoryListener(RepositoryListener listener) {
		listeners.remove(listener);
	}

	protected void fireChangeEvent(E instance, boolean isDeleted, boolean isAdded, boolean isUpdated){
		RepositoryChangeEvent<E> event = new RepositoryChangeEvent<E>(
				this,
				instance,
				isDeleted, isAdded, isUpdated
		);
		
		for (RepositoryListener listener : listeners){
			listener.onRepositoryChanged(event);	
		}
		
		event = null;
	}
	
	protected void fireAddedEvent(E instance){
		fireChangeEvent(instance,false,true,false);
	}
	
	protected void fireUpdatedEvent(E instance){
		fireChangeEvent(instance,false, false, true);
	}
	
	protected void fireRemoveEvent(E instance){
		fireChangeEvent(instance,true, false, false);
	}
	
	public Query<E> findIdentical(E instance) {
		return findByIdentity(this.getIdentityFor(instance));
	}
	
	public Query<E> findEquals(final E instance) {
		
		return new ListQuery<E>(){

			@Override
			protected List<E> list() {
				List<E> all = new ArrayList<E>(findAll().fetchAll());
				for (Iterator<E> it = all.iterator(); it.hasNext();){
					if (!it.next().equals(instance)){
						it.remove();
					}
				}
				return all;
			}
			
		};
		
	}

	protected DomainModel getDomainModel(){
		return this.domainModel;
	}
}