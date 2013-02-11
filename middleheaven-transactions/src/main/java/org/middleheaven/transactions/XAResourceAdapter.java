package org.middleheaven.transactions;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

public abstract class XAResourceAdapter implements XAResource {

    private Xid xid;

    public Xid[] recover(int flag) throws XAException {
        return new Xid[]{xid};
    }
    
    protected Xid getXid(){
    	return xid;
    }

	public void start(Xid xid, int flag) throws XAException {
        // Associate to transaction  
        this.xid = xid;
    }

    public void end(Xid xid, int flag) throws XAException {
        // Disassociate from transaction
        this.xid = null;
    }

    public int prepare(Xid xid) throws XAException {
        return XA_OK;
    }
    
    public void forget(Xid xid) throws XAException {
        // Heuristic support - n/a

    }

    public int getTransactionTimeout() throws XAException {
        return 0;
    }

    public boolean setTransactionTimeout(int arg0) throws XAException {
        return false;
    }

    public boolean isSameRM(XAResource xa) throws XAException {
        return this == xa;
    }

 


}
