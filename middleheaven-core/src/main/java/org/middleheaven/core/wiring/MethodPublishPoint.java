package org.middleheaven.core.wiring;

import java.util.Map;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedMethod;


public final class MethodPublishPoint extends AbstractMethodWiringPoint implements PublishPoint {

	private ReflectedMethod method;
	private Map<String, Object> params;
	private Enumerable<WiringSpecification> paramsSpecifications;
	private String scope;

	public MethodPublishPoint(ReflectedMethod method , Map<String,Object> params, String scope,  Enumerable<WiringSpecification> paramsSpecifications) {
		this.method = method;
		this.params = params;
		this.scope = scope;
		this.paramsSpecifications = paramsSpecifications;
	}

	
	public Enumerable<WiringSpecification> getSpecifications(){
		return paramsSpecifications;
	}
	
	@Override
	public Object getObject(InstanceFactory factory, Object publisherObject) {
		
		return this.callMethodPoint(factory, method, publisherObject, paramsSpecifications);

	}

	@Override
	public ReflectedClass<?> getPublishedType() {
		return method.getReturnType();
	}

	@Override
	public Map<String, Object> getParams() {
		return params;
	}

	public String toString(){
		return method.getDeclaringClass().getName() + "#" + method.getName();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getScope() {
		return this.scope;
	}

}
