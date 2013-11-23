package org.middleheaven.core.services;

import java.util.Map;

import org.middleheaven.util.Maybe;


/**
 * Primary interface to retrive and register services from {@link ServiceActivator}s.
 * 
 * 
 */
public interface ServiceContext {

	/**
	 * Register a service in the context.
	 * Its a simples contract-implementation registration.
	 * Diferent implementations of the same contract can be distinguish by the use of properties.
	 * these properties can them be queried to find the correct implementations. A classic example is to print services, one 
	 * that prints color and another that don't.
	 * 
	 * @param contractInterface the service contract interface type.
	 * @param implementation the contract implementation
	 * @param properties the properties related with this implementations.
	 */
	public  <T,I extends T> void register(Class<T> contractInterface, I implementation , Map<String, ? extends Object> properties );
	
	
	/**
	 * Register a service in the context.
	 * Its a simples contract-implementation registration, with no implementations properties.
	 * 
	 * @param contractInterface the service contract interface type.
	 * @param implementation the contract implementation
	 *
	 */
	public  <T,I extends T> void register(Class<T> contractInterface, I implementation );
	
	/**
	 * Unregisters a service from the context.
	 * The service will be macthed with the given properties ans contractInterface
	 * 
	 * @param contractInterface the service contract interface type.
	 * @param implementation the contract implementation
	 * @param properties the properties related with this implementations.
	 */
	public  <T,I extends T> void unRegister(Class<T> contractInterface, I implementation ,Map<String, ? extends Object> properties );
	
	/**
	 * Unregisters a service from the context.
	 *
	 * @param contractInterface the service contract interface type.
	 * @param implementation the contract implementation
	 * 
	 */
	public  <T,I extends T> void unRegister(Class<T> contractInterface, I implementation );

	/**
	 * Unregisters a service implementations from the context.
	 * This could unregister several contractInterfaces.
	 * 
	 * @param implementation the implementation to unregister. 
	 */
	public void unRegister(Object implementation);
	
	/**
	 * Unregisters all implementations of a given contract interface.
	 * 
	 * @param contractInterface the contract interface to unregister.
	 */
	public void unRegister(Class<?> contractInterface);

	/**
	 * Obtain an implementation of the given contract interface that satisfies the given properties.
	 * 
	 * @param contractInterface the contract interface to find.
	 * @param properties the properties that qualify the implementation
	 * @return the found implementation of the given contract interface
	 */
	public <T> T getService (Class<T> contractInterface,Map<String, ? extends Object> properties);
	
	/**
	 * Obtain any implementation of the given contract interface.
	 * 
	 * @param contractInterface the contract interface to find.
	 * @return the found implementation of the given contract interface
	 * @throws ServiceNotAvailableException if the service is unavailable
	 */
	public <T> T getService (Class<T> contractInterface) throws ServiceNotAvailableException;

	/**
	 * Obtain any implementation of the given contract interface.
	 * 
	 * @param contractInterface the contract interface to find.
	 * @return the found implementation of the given contract interface
	 */
	public <T> Maybe<T> getPossibleUnAvailableService (Class<T> contractInterface);

}
