/**
 * 
 */
package org.middleheaven.core.wiring;

import java.lang.reflect.Method;

import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.core.reflection.ReflectionException;

/**
 * 
 */
public class AbstractMethodWiringPoint {

	
	protected final Object callMethodPoint(InstanceFactory factory, MethodHandler method, Object object, WiringSpecification[] paramsSpecifications){
	
		Object[] params = new Object[paramsSpecifications.length];
		
		Class<?>[] parameterTypes = method.getParameterTypes();
		
		for (int i=0;i<paramsSpecifications.length;i++){
			try {
				
				WiringTarget target = new ParameterWiringTarget(parameterTypes[i], method.getDeclaringClass(), object);
				
				params[i] = factory.getInstance(WiringQuery.search(paramsSpecifications[i]).on(target));
				
				if (params[i]== null && paramsSpecifications[i].isRequired()){
					throw new BindingException("Can not find an instance of " + paramsSpecifications[i].getContract() + " to bind with parameter of index " + i +  
							" in method " + method.getDeclaringClass().getName() +"." + 
							method.getName()+ " was not found ");
				}
			} catch (BindingNotFoundException e){
				throw new BindingException("Can not find an instance of " + paramsSpecifications[i].getContract() + " to bind with parameter of index " + i +  
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
