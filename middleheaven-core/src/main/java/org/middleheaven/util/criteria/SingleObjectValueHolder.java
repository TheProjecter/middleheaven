package org.middleheaven.util.criteria;

import java.util.Map;

import org.middleheaven.collections.ParamsMap;

/**
 * Holds a single value.
 */
public class SingleObjectValueHolder implements FieldValueHolder {

	private static final long serialVersionUID = 2756122283381728051L;
	
	private Object value;
	private Map<String,String> params = new ParamsMap();
	
	/**
	 * 
	 * Constructor.
	 * @param value the value
	 */
	public SingleObjectValueHolder(Object value) {
		this.value = value;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue() {
		return value;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equalsValue(FieldValueHolder valueHolder) {
		return value.equals(valueHolder.getValue());
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getParam(String name) {
		return params.get(name);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setParam(String name, String value) {
		params.put(name, value);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		return String.valueOf(value);
	}

}
