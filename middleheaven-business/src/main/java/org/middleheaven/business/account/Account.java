package org.middleheaven.business.account;

import java.util.List;

import org.middleheaven.collections.interval.Interval;
import org.middleheaven.quantity.measure.Amount;
import org.middleheaven.quantity.time.DateHolder;

public abstract class Account<A extends Amount<A,?>> {

	private AccountOwner owner;
	private AccountType type;
	private AccountRepository<A> repository;
	
    void setAccountRepository(AccountRepository<A> repository){
		this.repository = repository;
	}
	
	public AccountOwner getOwner() {
		return owner;
	}
	public void setOwner(AccountOwner owner) {
		this.owner = owner;
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
	
	public final A getBalance(DateHolder date){
		return repository.getAccountBalance(this, date);
	}
	
	public final <M extends AccountMovement<A>> List<M> getMovements(Interval<? extends DateHolder> period){
		return repository.getAccountMovements(this, period);
	}
}
