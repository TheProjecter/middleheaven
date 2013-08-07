package org.middleheaven.core.wiring;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.middleheaven.core.reflection.MethodHandler;

public final class MethodPublishPoint extends AbstractMethodWiringPoint implements PublishPoint {

	private MethodHandler method;
	private Map<String, Object> params;
	private List<WiringSpecification> paramsSpecifications;
	private String scope;

	public MethodPublishPoint(MethodHandler method , Map<String,Object> params, String scope,  WiringSpecification[] paramsSpecifications) {
		this.method = method;
		this.params = params;
		this.scope = scope;
		this.paramsSpecifications = Arrays.asList(paramsSpecifications);
	}

	
	public List<WiringSpecification> getSpecifications(){
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
