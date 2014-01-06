/**
 * 
 */
package org.middleheaven.reflection;


public class OriginalMethodHandler implements ProxyHandler {

	
	public OriginalMethodHandler(){}
	
	@Override
	public Object invoke(Object self, Object[] args, MethodDelegator delegator)throws Throwable {
		return delegator.invokeSuper(self, args);  // execute the original method.
	}
}