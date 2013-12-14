package org.middleheaven.core.wiring;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.reflection.ReflectedMethod;


/**
 * Used a {@link Method} as a {@link AfterWiringPoint}.
 */
public class MethodAfterWiringPoint extends AbstractMethodWiringPoint implements AfterWiringPoint{

	private ReflectedMethod method;
	private WiringSpecification methodSpecification;
	private Enumerable<WiringSpecification> paramsSpecifications;

	//	public WiringSpecification<?> getMethodSpecification() {
	//		return methodSpecification;
	//	}
	//
	//	public WiringSpecification<?>[] getParamsSpecifications() {
	//		return paramsSpecifications;
	//	}

	
	public Enumerable<WiringSpecification> getSpecifications(){
		return paramsSpecifications;
	}
	
	/**
	 * 
	 * Constructor.
	 * @param method the method used to wire objects.
	 * @param methodSpecification the wiring specification of the method. 
	 * @param paramsSpecifications the wiring specification for each method parameter.
	 */
	public MethodAfterWiringPoint(ReflectedMethod method, WiringSpecification methodSpecification, Enumerable<WiringSpecification> paramsSpecifications) {
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
