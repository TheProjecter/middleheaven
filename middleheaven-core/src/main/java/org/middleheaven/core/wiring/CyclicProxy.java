package org.middleheaven.core.wiring;

import org.middleheaven.reflection.MethodDelegator;
import org.middleheaven.reflection.ProxyHandler;


public class CyclicProxy implements ProxyHandler{

	private Object realObject;
	
	protected void setRealObject(Object realObject) {
		this.realObject = realObject;
	}

	@Override
	public Object invoke(Object proxy, Object[] args, MethodDelegator delegator)throws Throwable {
		if (realObject==null){
			throw new InstantiationException("Cyclic Reference was not resolved before invoking " + delegator.getInvoked().getName());
		} else {
			return delegator.invoke(realObject,args);
		}
	}

}
