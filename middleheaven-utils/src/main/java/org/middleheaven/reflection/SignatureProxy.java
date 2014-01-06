package org.middleheaven.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

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

		try {
			Method method = original.getClass().getMethod(
					delegator.getName(), 
					delegator.getInvoked().getParameterTypes()
					);

			return method.invoke(original, args);
		} catch (NoSuchMethodException e){
			throw new ReflectionException("Method " + delegator.getName() + Arrays.toString(delegator.getInvoked().getParameterTypes()) + " not found in " + original.getClass());
		}


	}

}
