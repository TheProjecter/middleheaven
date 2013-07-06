package org.middleheaven.core.reflection;

import org.middleheaven.collections.Enumerable;

public interface ReflectionStrategy {

	public PropertyHandler getPropertyAccessor(Class<?> type, String fieldName);
	public Enumerable<PropertyHandler> getPropertyAccessors(Class<?> type, boolean inherit) throws ReflectionException;

    /**
     * 
     * @param facadeClass
     * @param handler
     * @param constructorArgs
     * @return
     */
	public <T> T proxyType (Class<T> facadeClass , ProxyHandler handler, Object[] constructorArgs);
	
	/**
	 * Applies a given interface to an object. The object can be invoke as if it implemented the interface
	 * @param delegationTarget
	 * @param proxyInterface
	 * @return
	 */
	public <I> I proxyObject(Object delegationTarget , Class<I> proxyInterface);
	
	/**
	 * Determine the real class type. 
	 * Removes all proxies created by this strategy and determines the real class.
	 * @param type
	 * @return
	 */
	public Class<?> getRealType(Class<?> type);
	
	/**
	 * 
	 * @param <I>
	 * @param delegationTarget
	 * @param handler
	 * @param proxyInterface
	 * @return an object that is instanceof {@code delegationTarget} and {@code proxyInterface}. Also this object implements {@code WrapperProxy} interface
	 */
	public <I> I proxyObject (Object delegationTarget ,  ProxyHandler handler , Class<I> proxyInterface , Class<?> ... adicionalInterfaces);
	
	public <T> T proxyType ( Class<?> facadeType, ProxyHandler handler , Class<T> proxyInterface  , Class<?> ... adicionalInterfaces);
	
	/**
	 * 
	 * @param type
	 * @return {@code true} if the type was been altered.
	 */
	public boolean isEnhanced(Class<?> type);
}
