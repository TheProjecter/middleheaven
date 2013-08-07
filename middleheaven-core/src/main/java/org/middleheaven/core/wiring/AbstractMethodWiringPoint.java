/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.core.reflection.ReflectionException;

/**
 * 
 */
public class AbstractMethodWiringPoint {

	/**
	 * 
	 * @param factory
	 * @param method
	 * @param object
	 * @param paramsSpecifications a list with the parameters specifications. 
	 * @return
	 */
	protected final Object callMethodPoint(InstanceFactory factory, MethodHandler method, Object object, List<WiringSpecification> paramsSpecifications){
	
		paramsSpecifications = CollectionUtils.ensureRandomAcess(paramsSpecifications);
		
		Object[] params = new Object[paramsSpecifications.size()];
		
		Class<?>[] parameterTypes = method.getParameterTypes();
		
		for (int i=0;i<paramsSpecifications.size();i++){
			final WiringSpecification paramSpec = paramsSpecifications.get(i);
			try {
				
				WiringTarget target = new ParameterWiringTarget(parameterTypes[i], method.getDeclaringClass(), object);
				
				params[i] = factory.getInstance(WiringQuery.search(paramSpec).on(target));
				
				if (params[i]== null && paramSpec.isRequired()){
					throw new BindingException("Can not find an instance of " + paramSpec.getContract() + " to bind with parameter of index " + i +  
							" in method " + method.getDeclaringClass().getName() +"." + 
							method.getName()+ " was not found ");
				}
			} catch (BindingNotFoundException e){
				throw new BindingException("Can not find an instance of " + paramSpec.getContract() + " to bind with parameter of index " + i +  
						" in method " + method.getDeclaringClass().getName() +"." + 
						method.getName()+ " was not found ");
			}
		}

		try {

			return method.invoke(object, params);
		} catch (Exception e) {
			throw ReflectionException.manage(e, object.getClass());
		} 

	}
}
