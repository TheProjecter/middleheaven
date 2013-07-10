/**
 * 
 */
package org.middleheaven.domain.query;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class QueryParametersBag {

	
	private Map<String, Object> values = new HashMap<String, Object>();

	/**
	 * @param parameterName
	 * @param parameterValueType
	 */
	public void setParameter(String parameterName, Object parameterValueType) {
		values.put(parameterName, parameterValueType);
	}
	
}
