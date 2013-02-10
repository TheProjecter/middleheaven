package org.middleheaven.core.reflection.inspection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.cglib.proxy.Dispatcher;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.WrapperProxy;


public class CGLibReflectionStrategy extends AbstractReflectionStrategy{

	
	private class ProxyMethodDelegator implements MethodDelegator {

		private Class<? extends Object> type;
		private Method invoked;
		private MethodProxy methodProxy;

		public ProxyMethodDelegator(Class<? extends Object> type,Method invoked, MethodProxy methodProxy) {
			this.type = type;
			this.invoked = invoked;
			this.methodProxy= methodProxy;
		}

		@Override
		public Method getInvoked() {
			return invoked;
		}

		@Override
		public String getName() {
			return invoked.getName();
		}

		@Override
		public boolean hasSuper() {
			try{
				type.getDeclaredMethod(invoked.getName(), invoked.getParameterTypes());
				return true;
			} catch (NoSuchMethodException e){
				return false;
			}
		}

		@Override
		public Object invoke(Object target, Object[] args) throws Throwable {
			return methodProxy.invoke(target, args);
		}

		@Override
		public Object invokeSuper(Object target, Object[] args) throws Throwable {
			return methodProxy.invokeSuper(target, args);
		}
		
	}
	private class ProxyHandlerInterceptor implements MethodInterceptor{

		private ProxyHandler handler;
		private Class<?> originalType;
		
		public ProxyHandlerInterceptor(Class<?> originalType, ProxyHandler handler) {
			super();
			this.originalType = originalType;
			this.handler = handler;
		}

		@Override
		public Object intercept(Object proxy, Method invoked, Object[] args,MethodProxy methodProxy) throws Throwable {
			try{
				return handler.invoke(proxy, args, new ProxyMethodDelegator(originalType,invoked,methodProxy));
			} catch (InvocationTargetException e){
				throw e.getTargetException();
			}
		}

	}
	

	@Override
	public <T> T proxyType(Class<T> facadeClass, ProxyHandler handler) {
		
		if (facadeClass == null){
			throw new IllegalArgumentException("facadeClass is required");
		}
		
		if (handler == null){
			throw new IllegalArgumentException("handler is required");
		}
	
		try{
			return facadeClass.cast(Enhancer.create(
					facadeClass,
					new ProxyHandlerInterceptor(facadeClass,handler)
			));
		}catch (RuntimeException e){
			throw new ReflectionException("Not possible to proxy type " + facadeClass + "." + e.getMessage());
		}
	}
	
	@Override
	public <T> T proxyType(Class<?> facadeType, ProxyHandler handler, Class<T> proxyInterface, Class<?>... adicionalInterfaces) {
		try{
			
			if (facadeType.isInterface()){
				Class[] interfaces = new Class[adicionalInterfaces.length + 2];
				interfaces[0] = facadeType;
				interfaces[1] = proxyInterface;
				for (int i =0; i < adicionalInterfaces.length; i++){
					interfaces[i+1] = adicionalInterfaces[i];
				}
				
				return proxyInterface.cast(Enhancer.create(
						Object.class,
						interfaces,
						new ProxyHandlerInterceptor(facadeType,handler)
				));
			} else {
				Class[] interfaces = new Class[adicionalInterfaces.length + 1];
				interfaces[0] = proxyInterface;

				for (int i =0; i < adicionalInterfaces.length; i++){
					interfaces[i+1] = adicionalInterfaces[i];
				}
				
				return proxyInterface.cast(Enhancer.create(
						facadeType,
						interfaces,
						new ProxyHandlerInterceptor(facadeType,handler)
				));
			}
			
		}catch (RuntimeException e){
			throw new ReflectionException(e);
		}
	}

	

	@Override
	public <I> I proxyObject(final Object delegationTarget, Class<I> proxyInterface) {
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}

		try{
			return proxyInterface.cast(Enhancer.create(
					delegationTarget.getClass(), 
					new Class[]{proxyInterface, WrapperProxy.class},
					new Dispatcher(){
						public Object loadObject(){
							return delegationTarget;
						}
					}
			));
		}catch (RuntimeException e){
			throw new ReflectionException(e);
		}
	}

	
	@Override
	public <I> I proxyObject(Object delegationTarget, ProxyHandler handler,Class<I> proxyInterface,Class<?> ... adicionalInterfaces) {
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}

		if (adicionalInterfaces.length==0 && proxyInterface.isInstance(delegationTarget)){
			return proxyObject(delegationTarget,proxyInterface);
		} else {
			try{
				@SuppressWarnings("unchecked") Class[] newInterfaces = new Class[adicionalInterfaces.length+1];
				newInterfaces[0] = proxyInterface;
				for (int i=0; i < adicionalInterfaces.length ;i ++){
					if (!adicionalInterfaces[i].isInterface()){
						throw new IllegalArgumentException("Proxy must be applied with an interface");
					}
					newInterfaces[i+1] = adicionalInterfaces[i];
				}
				return proxyInterface.cast(Enhancer.create(
						delegationTarget.getClass(), 
						newInterfaces,
						new ProxyHandlerInterceptor(delegationTarget.getClass(),handler))
				);
			} catch (IllegalArgumentException e){
				if (e.getMessage().equals("Superclass has no null constructors but no arguments were given")){
					throw new ReflectionException("Type " + delegationTarget.getClass() + " has no argumentless constructors and no arguments were given");
				} else {
					throw new ReflectionException(e);
				}
			}catch (RuntimeException e){
				throw new ReflectionException(e);
			}
		}
	


	}

	@Override
	public Class<?> getRealType(Class<?> type) {
		int pos = type.getName().indexOf("$$");
		if (pos >=0){
			 try {
				return Class.forName(type.getName().substring(0, pos));
			} catch (ClassNotFoundException e) {
				throw ReflectionException.manage(e, type);
			}
		}
		return type;
	}

	@Override
	public boolean isEnhanced(Class<?> type) {
		return Enhancer.isEnhanced(type);
	}

	




}
