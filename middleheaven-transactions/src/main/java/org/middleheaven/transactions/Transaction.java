package org.middleheaven.transactions;

public interface Transaction {

	public void begin();

	public void commit();

	public void roolback();
	
	public boolean isActive();
}
