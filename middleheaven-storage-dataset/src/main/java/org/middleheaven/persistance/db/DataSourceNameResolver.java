/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.DataStoreName;

/**
 * 
 */
public interface DataSourceNameResolver {

	
	public String resolveDataSourceName(DataStoreName name);
}
