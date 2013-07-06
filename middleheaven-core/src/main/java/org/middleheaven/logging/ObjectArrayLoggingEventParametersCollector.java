/**
 * 
 */
package org.middleheaven.logging;

import org.middleheaven.collections.CollectionUtils;

/**
 * 
 */
public class ObjectArrayLoggingEventParametersCollector implements
		LoggingEventParametersCollector {

	
	private Object[] params;
	
	public ObjectArrayLoggingEventParametersCollector (Object[] params){
		this.params = CollectionUtils.duplicateArray(params);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] collect() {
		return params;
	}

}
