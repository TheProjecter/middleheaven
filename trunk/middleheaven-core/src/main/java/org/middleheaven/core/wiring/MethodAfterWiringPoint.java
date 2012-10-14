package org.middleheaven.core.wiring;

import java.lang.reflect.Method;

/**
 * Used a {@link Method} as a {@link AfterWiringPoint}.
 */
public class MethodAfterWiringPoint extends AbstractMethodWiringPoint implements AfterWiringPoint{

	private Method method;
	private WiringSpecification methodSpecification;
	private WiringSpecification[] paramsSpecifications;

	//	public WiringSpecification<?> getMethodSpecification() {
	//		return methodSpecification;
	//	}
	//
	//	public WiringSpecification<?>[] getParamsSpecifications() {
	//		return paramsSpecifications;
	//	}

	
	public WiringSpecification[] getSpecifications(){
		return paramsSpecifications;
	}
	
	/**
	 * 
	 * Constructor.
	 * @param method the method used to wire objects.
	 * @param methodSpecification the wiring specification of the method. 
	 * @param paramsSpecifications the wiring specification for each method parameter.
	 */
	public MethodAfterWiringPoint(Method method, WiringSpecification methodSpecification, WiringSpecification[] paramsSpecifications) {
		super();
		this.method = method;
		this.methodSpecification = methodSpecification;
		this.paramsSpecifications = paramsSpecifications;
	}


	@Override
	public String toString(){
		return method.getName();
	}

	@Override
	public int hashCode(){
		return method.hashCode();
	}

	@Override
	public boolean equals(Object other){
		return other instanceof MethodAfterWiringPoint && this.method.equals(((MethodAfterWiringPoint)other).method);
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	public <T> T writeAtPoint(InstanceFactory factory, T object){
		if(object ==null){
			return null;
		}

		callMethodPoint(factory, method, object, paramsSpecifications);
	
		return object;
	}
	
	

	@Override
	public boolean isRequired() {
		for (WiringSpecification ws : paramsSpecifications){
			if (ws.isRequired()){
				return true;
			}
		}
		return false;
	}
}