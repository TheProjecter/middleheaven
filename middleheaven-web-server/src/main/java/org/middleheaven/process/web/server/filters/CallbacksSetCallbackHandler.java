/**
 * 
 */
package org.middleheaven.process.web.server.filters;

import java.io.Serializable;

import org.middleheaven.aas.AccessException;
import org.middleheaven.aas.CallbackHandler;
import org.middleheaven.aas.CallbacksSet;

/**
 * 
 */
public class CallbacksSetCallbackHandler implements CallbackHandler , Serializable{


	private static final long serialVersionUID = 6897036338677468538L;
	
	final CallbacksSet set;
	
	public CallbacksSetCallbackHandler (){
		this.set = new CallbacksSet();
	}
	
	public CallbacksSetCallbackHandler (CallbacksSet other){
		this.set = other;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CallbacksSet getCallbacks() {
		return set;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onException(AccessException e) {
		//no-op
	}

}
