package org.middleheaven.storage;

import org.middleheaven.storage.criteria.Criteria;
import org.middleheaven.util.identity.Identity;

public interface DataStorage {

	public Identity getIdentityFor(Object object);
	public <T> T store(T obj);
	public <T> void remove(T obj);
	public <T> Query<T> createQuery(Criteria<T> criteria);
	public <T> Query<T> createQuery(Criteria<T> criteria, ReadStrategy strategy);
	public <T> void remove(Criteria<T> criteria);
	
	public void addStorageListener(DataStorageListener listener);
	public void removeStorageListener(DataStorageListener listener);
	
}