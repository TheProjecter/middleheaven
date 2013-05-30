package org.middleheaven.business.account;

import java.util.Collections;
import java.util.List;

import org.middleheaven.collections.Interval;
import org.middleheaven.domain.store.DomainStore;
import org.middleheaven.quantity.measure.Amount;
import org.middleheaven.quantity.time.DateHolder;

public abstract class AccountRepository<A extends Amount<A,?>> {

	private DomainStore ds;
	private A zero;
	
	public AccountRepository(A zero){
		this.zero = zero;
	}
	
	protected void setDataStorage (DomainStore ds){
		this.ds = ds;
	}
	
	public <C extends Account<A>> List<C> getOwnerAccounts(AccountOwner owner){
		return getOwnerAccounts(owner, null);
	}
	
	public A getAccountBalance ( Account<A> account, DateHolder date){
		List<AccountMovement<A>> movements = this.getAccountMovements(account, date, date);
		
		A total = zero;
		for ( AccountMovement<A> m : movements){
			total = total.plus(m.getAmount());
		}

		return total;
	}
	
	public final <M extends AccountMovement<A>> List<M> getAccountMovements( Account<A> account,Interval<? extends DateHolder> period){
		return getAccountMovements(account, period.start(),period.end());
	}
	
	private final <M extends AccountMovement<A>> List<M> getAccountMovements( Account<A> account,DateHolder inicialDate, DateHolder finalDate){
		return Collections.emptyList(); // TODO
	}
	
	public <C extends Account<A>> List<C> getOwnerAccounts(AccountOwner owner, AccountType type){
		return Collections.emptyList(); // TODO
	}
	
}
