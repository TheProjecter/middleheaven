package org.middleheaven.storage;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.domain.DomainModel;
import org.middleheaven.sequence.DefaultToken;
import org.middleheaven.sequence.Sequence;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.identity.Identity;
import org.middleheaven.util.identity.IdentitySequence;

public class DomainDataStorage implements DataStorage {

	StoreKeeper storeKeeper;
	DomainModel domainModel;
	Set<DataStorageListener> listeners = new CopyOnWriteArraySet<DataStorageListener>();
	
	public DomainDataStorage(StoreKeeper storeManager,DomainModel domainModel) {
		this.storeKeeper = storeManager;
		this.domainModel = domainModel;
	}

	public final Identity getIdentityFor(Object object){
		if (object instanceof Storable){
			return ((Storable)object).getIdentity();
		}
		return null;
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria) {
		return this.createQuery(criteria,ReadStrategy.none());
	}

	@Override
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy) {
		return storeKeeper.createQuery(criteria, 
				storeKeeper.storableModelOf(domainModel.getEntityModelFor(criteria.getTargetClass())) ,
				strategy
		);
	}

	@Override
	public <T> T store(T obj) {
	
		Storable p = this.storeKeeper.merge(obj);
		doStore(p);
		return (T)p;
	}

	private final void doStore(Storable p){
		switch (p.getPersistableState()){
		case DELETED:
			doDelete(p);
			break;
		case EDITED:
			doUpdate(p);
			break;
		case FILLED:
			doInsert(p);
			break;
		case BLANK:
		case RETRIVED:
			// not-op
			break;
		default:
			throw new IllegalStateException(p.getPersistableState() + " is unkown");
		}
	}

	private static class DataBaseIntegerIdentitySequence implements IdentitySequence<Identity> {

		Sequence<Identity> baseSequence;

		public DataBaseIntegerIdentitySequence(Sequence<Identity> baseSequence) {
			super();
			this.baseSequence = baseSequence;
		}

		@Override
		public SequenceToken<Identity> next() {
			// TODO define LongIdentity
			return new DefaultToken<Identity>(baseSequence.next().value());
		}


	}

	private void doInsert(Storable p) {
		if (p.getIdentity()!=null){
			doUpdate(p);
		}

		// assign key
		this.storeKeeper.assignIdentity(p);

		
		this.storeKeeper.insert(
				Collections.singleton(p),
				storeKeeper.storableModelOf(domainModel.getEntityModelFor(p.getPersistableClass()))
		);

		p.setPersistableState(PersistableState.RETRIVED);
		fireAddEvent(p);
	}

	private void doUpdate(Storable p) {
		if (p.getIdentity()==null){
			doInsert(p);
		}

		this.storeKeeper.update(
				Collections.singleton(p),
				storeKeeper.storableModelOf(domainModel.getEntityModelFor(p.getPersistableClass()))
		);

		p.setPersistableState(PersistableState.RETRIVED);
		fireUpdatedEvent(p);
	}

	private void doDelete(Storable p) {
		if (p.getIdentity()==null){
			return;
		}

		this.storeKeeper.remove(
				Collections.singleton(p),
				storeKeeper.storableModelOf((domainModel.getEntityModelFor(p.getPersistableClass())))
		);

		p.setPersistableState(PersistableState.DELETED);
		fireRemovedEvent(p);
	}



	@Override
	public <T> void remove(T obj) {
		if (obj instanceof Storable){
			Storable p = (Storable)obj;
			p.setPersistableState(PersistableState.DELETED);
			doStore(p);
		} 
		// else 
		// not managed
		// do nothing as this obj is not in the store

	}

	@Override
	public <T> void remove(Criteria<T> criteria) {
		this.storeKeeper.remove(criteria, 
				storeKeeper.storableModelOf(domainModel.getEntityModelFor(criteria.getTargetClass()))
		);
	}

	@Override
	public void addStorageListener(DataStorageListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeStorageListener(DataStorageListener listener) {
		listeners.remove(listener);
	}

	private void fireAddEvent(Object instance){
		StorageChangeEvent event = new StorageChangeEvent(instance,
				false, true, false
		);

		for (DataStorageListener listener : listeners){
			listener.onStorageChange(event);
		}

		event = null;
	}

	private void fireRemovedEvent(Object instance){
		StorageChangeEvent event = new StorageChangeEvent(instance,
				true, false, false
		);

		for (DataStorageListener listener : listeners){
			listener.onStorageChange(event);
		}

		event = null;
	}

	private void fireUpdatedEvent(Object instance){
		StorageChangeEvent event = new StorageChangeEvent(instance,
				false, false, true
		);

		for (DataStorageListener listener : listeners){
			listener.onStorageChange(event);
		}

		event = null;
	}
}