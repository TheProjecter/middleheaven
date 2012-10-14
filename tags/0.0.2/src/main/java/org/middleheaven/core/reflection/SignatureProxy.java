package org.middleheaven.core.reflection;

import java.lang.reflect.Method;

/**
 * Invokes methods on the underliyng object only based on their signature
 */
public class SignatureProxy implements ProxyHandler {

	private Object original;

	public SignatureProxy(Object original) {
		this.original = original;
	}

	@Override
	public Object invoke(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable {
		
		Method method = original.getClass().getMethod(
				delegator.getName(), 
				delegator.getInvoked().getParameterTypes()
		);
		
		if (method == null){
			throw new ReflectionException("Method " + delegator.getName() + delegator.getInvoked().getParameterTypes() + " not found in " + original.getClass());
		}
		
		return method.invoke(original, args);
	}

}