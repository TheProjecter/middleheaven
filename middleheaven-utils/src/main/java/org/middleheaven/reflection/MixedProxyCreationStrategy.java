/**
 * 
 */
package org.middleheaven.reflection;

import java.lang.reflect.Proxy;

import org.middleheaven.reflection.inspection.ProxyHandlerInvocationHandlerAdapter;


/**
 * 
 */
public class MixedProxyCreationStrategy extends CGLibProxyCreationStrategy{

	@Override
	public <I> I proxyObject(final Object delegationTarget, Class<I> proxyInterface) {
		if (!proxyInterface.isInterface()){
			throw new IllegalArgumentException("Proxy must be applied with an interface");
		}

		try{
			return proxyInterface.cast(Proxy.newProxyInstance(
					proxyInterface.getClassLoader(), 
					new Class[]{proxyInterface, WrapperProxy.class}, 
					new ProxyHandlerInvocationHandlerAdapter(new SignatureProxy(delegationTarget))
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
				for (int i=0; i < adicionalInterfaces.length ;i ++){
					if (!adicionalInterfaces[i].isInterface()){
						throw new IllegalArgumentException("Proxy must be applied with an interface");
					}
				}
				
				Class[] newInterfaces = new Class[adicionalInterfaces.length+2];
				newInterfaces[0] = proxyInterface;
				newInterfaces[1] = WrapperProxy.class;
				
				System.arraycopy(adicionalInterfaces, 0, newInterfaces, 2, adicionalInterfaces.length);
				
				return proxyInterface.cast(Proxy.newProxyInstance(
						proxyInterface.getClassLoader(), 
						new Class[]{proxyInterface, WrapperProxy.class}, 
						new ProxyHandlerInvocationHandlerAdapter(new SignatureProxy(delegationTarget))
				));
				
			} catch (IllegalArgumentException e){
					throw new ReflectionException(e);
			}catch (RuntimeException e){
				throw new ReflectionException(e);
			}
		}



	}

}
