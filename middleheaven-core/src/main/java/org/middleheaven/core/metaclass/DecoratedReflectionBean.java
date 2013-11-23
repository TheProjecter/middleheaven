package org.middleheaven.core.metaclass;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.PropertyNotFoundException;

/**
 * MetaBean that adds properties to another MetaBean.
 */
public class DecoratedReflectionBean implements MetaBean {

	private MetaBean originalBean;
	private DecoratedMetaClass decoratedMetaClass;
	private MetaClass originalClass;
	
	private Map<String, Object> values = new HashMap<String, Object>();

	public DecoratedReflectionBean(MetaBean originalBean, MetaClass original,
			DecoratedMetaClass decoratedMetaClass) {
		
		this.originalBean = originalBean;
		this.originalClass = original;
		this.decoratedMetaClass = decoratedMetaClass;
	}

	@Override
	public MetaClass getMetaClass() {
		return decoratedMetaClass;
	}

	@Override
	public Object get(String name) {
		if (originalClass.containsProperty(name)){
			return originalBean.get(name);
		} else if (decoratedMetaClass.containsProperty(name)){
			return values.get(name);
		} else {
			throw new PropertyNotFoundException(name , this.getMetaClass().getName());
		}
	}

	@Override
	public void set(String name, Object value) {
		if (originalClass.containsProperty(name)){
			originalBean.set(name, value);
		} else if (decoratedMetaClass.containsProperty(name)){
			values.put(name, value);
		} else {
			throw new PropertyNotFoundException( name , this.getMetaClass().getName());
		}
	}

}
