package org.middleheaven.core.wiring;

import java.util.Map;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.reflection.ReflectedClass;

public interface PublishPoint extends WiringPoint {

	public Enumerable<WiringSpecification> getSpecifications();
	
	public ReflectedClass<?> getPublishedType();
	
	public Object getObject(InstanceFactory factory, Object publisherObject);
	
	public Map<String, Object> getParams();

	public String getScope(); 
}
