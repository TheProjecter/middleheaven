/**
 * 
 */
package org.middleheaven.core.wiring;

import java.util.List;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.Pair;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectedParameter;

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
	protected final Object callMethodPoint(InstanceFactory factory, ReflectedMethod method, Object object, Enumerable<WiringSpecification> paramsSpecifications){
	
		Object[] params = new Object[paramsSpecifications.size()];
		
		for( Pair<ReflectedParameter,WiringSpecification> pair : method.getParameters().join(paramsSpecifications))
		{
			final WiringSpecification paramSpec = pair.right();
			final ReflectedParameter parameter = pair.left();
			try {
				
				WiringTarget target = new ParameterWiringTarget(parameter.getType(), method.getDeclaringClass(), object);
				
				params[parameter.getIndex()] = factory.getInstance(WiringQuery.search(paramSpec).on(target));
				
				if (params[parameter.getIndex()]== null && paramSpec.isRequired()){
					throw new BindingException("Can not find an instance of " + paramSpec.getContract() + " to bind with parameter of index " + parameter.getIndex() +  
							" in method " + method.getDeclaringClass().getName() + "." + method.getName());
				}
			} catch (BindingNotFoundException e){
				throw new BindingException("Can not find an instance of " + paramSpec.getContract() + " to bind with parameter of index " + parameter.getIndex() +  
						" in method " + method.getDeclaringClass().getName() +"." + 
						method.getName()+ " was not found ");
			}
		}
		
		return method.invoke(object, params);
	}
}
