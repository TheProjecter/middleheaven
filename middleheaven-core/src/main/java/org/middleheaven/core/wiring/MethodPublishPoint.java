package org.middleheaven.core.wiring;

import java.lang.reflect.Method;
import java.util.Map;

public final class MethodPublishPoint extends AbstractMethodWiringPoint implements PublishPoint {

	private Method method;
	private Map<String, Object> params;
	private WiringSpecification[] paramsSpecifications;
	private String scope;

	public MethodPublishPoint(Method method , Map<String,Object> params, String scope,  WiringSpecification[] paramsSpecifications) {
		this.method = method;
		this.params = params;
		this.scope = scope;
		this.paramsSpecifications = paramsSpecifications;
	}

	
	public WiringSpecification[] getSpecifications(){
		return paramsSpecifications;
	}
	
	@Override
	public Object getObject(InstanceFactory factory, Object publisherObject) {
		
		return this.callMethodPoint(factory, method, publisherObject, paramsSpecifications);

	}

	@Override
	public Class<?> getPublishedType() {
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