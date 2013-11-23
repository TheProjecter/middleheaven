/*
 * Created on 2006/09/23
 *
 */
package org.middleheaven.sequence.persistent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.middleheaven.sequence.SequenceState;
import org.middleheaven.sequence.SequenceToken;
import org.middleheaven.sequence.StateEditableSequence;
import org.middleheaven.sequence.service.AbstractStatePersistanteSequence;
import org.middleheaven.transactions.TransactionService;
import org.middleheaven.transactions.XAResourceAdapter;
import org.middleheaven.util.Hash;


/**
 * @author  Sergio M. M. Taborda 
 */
public class TransactableSequence<T extends Comparable<? super T>> extends AbstractStatePersistanteSequence<T>  {

	private Object lastUsed;
	private StateEditableSequence<T> baseSequence;
	private  TransactionService service;
	
    BlockingQueue<TransactableSequenceValue<T>> queue = new PriorityBlockingQueue<TransactableSequenceValue<T>>();


	TransactableSequence(String name,StateEditableSequence<T> sequence ,  TransactionService service) {
		super(name);
		this.service = service;
		this.baseSequence = sequence;
	}
    
	@Override
	public SequenceState getSequenceState() {
		return new SequenceState(this.getName(),lastUsed);
	}

	@Override
	public void setSequenceState(SequenceState state) {
		this.lastUsed =  state.getLastUsedValue();
		this.baseSequence.setSequenceState(state);
	}
 
    public SequenceToken<T> next() {
        
        TransactableSequenceValue<T> sv = new TransactableSequenceValue<T>(baseSequence.next().value());
        queue.add(sv);
        
        // enlist as XAResource 
         service.enlistResource(sv); // occurs START
      
        
        return sv;
    }

    private class TransactableSequenceValue<I extends Comparable<? super I>> 
    	extends XAResourceAdapter 
    	implements SequenceToken<I>,Comparable<TransactableSequenceValue<I>>, XAResource {

        private I actualValue;
        
        TransactableSequenceValue(I actualValue){
            this.actualValue = actualValue;
        }

        public int compareTo(TransactableSequenceValue<I> other) {
            return this.actualValue.compareTo(other.actualValue);
        }
        
        
        public boolean equals(Object other){
			return (other instanceof TransactableSequenceValue) && equalsOther((TransactableSequenceValue) other);
		}

		/**
		 * @param other
		 * @return
		 */
		private boolean equalsOther(TransactableSequenceValue<I> other) {
			return this.actualValue.compareTo(other.actualValue) == 0;
		}
		
		public int hashCode(){
			return Hash.hash(actualValue).hashCode();
		}
		
        public String toString(){
            return actualValue.toString();
        }
        
        public I value() {

            while (queue.peek()!=this){
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                   return null;
                }
            }
            
            return actualValue;
 
        }

        public synchronized void commit(Xid xid, boolean onePhase) throws XAException {
            // do nothing
            lastUsed = this.actualValue;
            queue.remove(this);
            
            fireStateChange(new SequenceState(getName(),lastUsed));

        }

        public synchronized void rollback(Xid xid) throws XAException {
            
              // TODO
               
        }

    }





}
