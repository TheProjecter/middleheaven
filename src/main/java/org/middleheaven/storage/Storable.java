package org.middleheaven.storage;

public interface Storable {

	public PersistableState getPersistableState();
	public void setPersistableState(PersistableState state);
	public Long getKey();
	public void setKey(Long key);
	public Class<?> getPersistableClass();
}