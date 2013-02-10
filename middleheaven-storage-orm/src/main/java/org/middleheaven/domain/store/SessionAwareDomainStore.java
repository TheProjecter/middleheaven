package org.middleheaven.domain.store;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import org.middleheaven.domain.criteria.EntityCriteria;
import org.middleheaven.domain.query.Query;
import org.middleheaven.transactions.XAResourceAdapter;
import org.middleheaven.util.criteria.ReadStrategy;
import org.middleheaven.util.identity.Identity;

class SessionAwareDomainStore extends XAResourceAdapter implements DomainStore  {

	private final DomainStoreManager manager;
	private final StorageUnit unit = new ArrayStorageUnit();
	

	public SessionAwareDomainStore(DomainStoreManager manager) {
		this.manager = manager;
	}
	
	public Identity getIdentityFor(Object object) {
		return manager.getIdentityFor(object); 
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria) {
		return this.createQuery(criteria, ReadStrategy.fowardReadOnly());
	}

	@Override
	public <T> Query<T> createQuery(EntityCriteria<T> criteria, ReadStrategy strategy) {
		return manager.createQuery(criteria , strategy, unit);
	}


	@Override  
	public <T> T store(T obj) {
		return manager.store(obj, unit);
	}

	@Override
	public <T> void remove(T obj) {
		manager.remove(obj, unit);
	}

	@Override
	public <T> void remove(final EntityCriteria<T> criteria) {
		
		for (T t : manager.createQuery(criteria, ReadStrategy.fowardReadOnly(), unit).fetchAll()){
			manager.remove(t, unit);
		}
	}

	// listeners
	
	@Override
	public final void addStorageListener(DomainStoreListener listener) {
		manager.addStorageListener(listener);
	}

	@Override
	public final void removeStorageListener(DomainStoreListener listener) {
		manager.removeStorageListener(listener);
	}



    // XAResource 
	
    public synchronized int prepare(Xid xid) throws XAException {
        
    	unit.simplify();
    	
    	return super.prepare(xid);
    }


	@Override
	public synchronized void commit(Xid xid, boolean flag) throws XAException {
		manager.commit(unit);
	}


	@Override
	public synchronized void rollback(Xid xid) throws XAException {
		manager.roolback(unit);
	}



}
