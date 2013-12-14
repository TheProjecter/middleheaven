/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.reflection.MethodDelegator;
import org.middleheaven.reflection.ProxyHandler;

/**
 * 
 */
public class EntityManagerProxyHandler implements ProxyHandler {

	private MetaBeanEntityInstance instance;

	/**
	 * Constructor.
	 * @param p
	 */
	public EntityManagerProxyHandler(MetaBeanEntityInstance instance) {
		this.instance = instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object invoke(Object self, Object[] args, MethodDelegator delegator)
			throws Throwable {

			if (delegator.hasSuper()){
				String name= delegator.getName();
				
				if(name.startsWith("get")) {
					return instance.getBean().get(name.substring(3).toLowerCase());
				} else if(name.startsWith("is")) {
					return instance.getBean().get(name.substring(2).toLowerCase());
				} else if(name.startsWith("set") && args.length>0) {
					instance.getBean().set(name.substring(3).toLowerCase(), args[0]);
					return null;
				} else if(name.equals("toString")) {
					return instance.getBean().toString();
				}else {
					return delegator.invokeSuper(self, args);  // execute the original method.
				}			
			
			} else {
				return delegator.invoke(instance, args);
			}
			
		
	}

}
