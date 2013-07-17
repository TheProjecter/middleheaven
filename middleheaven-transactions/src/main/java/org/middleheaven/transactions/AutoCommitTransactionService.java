package org.middleheaven.transactions;

import java.util.Deque;
import java.util.LinkedList;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.middleheaven.logging.Logger;
import org.middleheaven.util.Hash;

/**
 * A {@link TransactionService} that auto commit evey change.
 */
public class AutoCommitTransactionService implements TransactionService {

	Deque<XAResource> xar = new LinkedList<XAResource>(); // order of insertion must be mantained
	ThreadLocal<MyTransaction> local = new ThreadLocal<MyTransaction>();

	/**
	 * 
	 * Constructor.
	 */
	public AutoCommitTransactionService(){

	}

	@Override
	public void enlistResource(XAResource xaResource) {
		final MyTransaction transaction = local.get();
		if (transaction != null && transaction.isActive ){
			try {
				xaResource.prepare(transaction.xid);
			} catch (XAException e) {
				throw new RuntimeException(e); // TODO handle better
			}
		}
		xar.addFirst(xaResource);
	}

	@Override
	public Transaction getTransaction() {
		MyTransaction t  = local.get();
		if (t ==null ){
			t = new MyTransaction(xar, local);
			local.set(t);
		}
		return t;
	}

	private static class LongXid implements Xid {

		long i = System.currentTimeMillis();

		@Override
		public byte[] getBranchQualifier() {
			// TODO implement Xid.getBranchQualifier
			return new byte[0];
		}

		@Override
		public int getFormatId() {
			// TODO implement Xid.getFormatId
			return 0;
		}

		@Override
		public byte[] getGlobalTransactionId() {
			// TODO implement Xid.getGlobalTransactionId
			return new byte[0];
		}

		/**
		 * 
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object other){
			return other instanceof LongXid && i == ((LongXid)other).i;
		}

		/**
		 * 
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode(){
			return Hash.hash(i).hashCode();
		}

	}

	private static class  MyTransaction implements Transaction{

		Xid xid;
		boolean isActive = false;
		private Deque<XAResource> xar;
		private ThreadLocal<MyTransaction> local;
		
		public MyTransaction(Deque<XAResource> xar , ThreadLocal<MyTransaction> local){
			xid = new LongXid();
			this.xar = xar;
			this.local = local;
		}

		@Override
		public void begin() {
			if (!isActive){
				try {
					for (XAResource xa : xar){
						xa.start(xid, 0);
					}
					isActive = true;
				} catch (XAException e) {
					Logger.onBookFor(this.getClass()).error("Error beginning", e);
					roolback();
				}
			}
		}

		@Override
		public void commit() {
			if (isActive){
				try {
					for (XAResource xa : xar){
						xa.prepare(xid);
					}

					for (XAResource xa : xar){
						xa.commit(xid, true);
					}

					for (XAResource xa : xar){
						xa.end(xid, 0);
					}

					local.set(null);
				} catch (XAException e) {
					Logger.onBookFor(this.getClass()).error("Error commiting", e);
					roolback();
				} finally {
					isActive = false;
				}
			}
		}

		@Override
		public void roolback() {
			try{
				for (XAResource xa : xar){
					try {
						xa.rollback(xid);
					} catch (XAException e) {
						Logger.onBookFor(this.getClass()).error("Error roolling back", e);
					}
				}

				for (XAResource xa : xar){
					try {
						xa.forget(xid);
					} catch (XAException e) {
						Logger.onBookFor(this.getClass()).error("Error roolling back", e);
					}
				}
			} finally {
				isActive = false;
			}

		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isActive() {
			return isActive;
		};
	}

	@Override
	public boolean isTransactional() {
		return local.get() != null;
	}
}
