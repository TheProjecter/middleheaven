/**
 * 
 */
package org.middleheaven.reflection.inspection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.middleheaven.reflection.MethodDelegator;
import org.middleheaven.reflection.ProxyHandler;

/**
 * 
 */
public class ProxyHandlerInvocationHandlerAdapter implements InvocationHandler {

	
	private ProxyHandler proxyHandler;

	public ProxyHandlerInvocationHandlerAdapter(ProxyHandler proxyHandler){
		this.proxyHandler = proxyHandler;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return proxyHandler.invoke(proxy, args, new NativeMethodDelegator(method));
	}

	
	private static class NativeMethodDelegator implements MethodDelegator{

		private Method method;

		public NativeMethodDelegator(Method method){
			this.method = method;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Method getInvoked() {
			return method;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getName() {
			return method.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasSuper() {
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object invokeSuper(Object target, Object[] args) throws Throwable {
			throw new UnsupportedOperationException("Not implememented");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object invoke(Object target, Object[] args) throws Throwable {
			return method.invoke(target, args);
		}
		
	}
}
