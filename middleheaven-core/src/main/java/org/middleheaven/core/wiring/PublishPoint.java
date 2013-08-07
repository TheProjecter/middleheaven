package org.middleheaven.core.wiring;

import java.util.List;
import java.util.Map;

public interface PublishPoint extends WiringPoint {

	public List<WiringSpecification> getSpecifications();
	
	public Class<?> getPublishedType();
	
	public Object getObject(InstanceFactory factory, Object publisherObject);
	
	public Map<String, Object> getParams();

	public String getScope(); 
}
