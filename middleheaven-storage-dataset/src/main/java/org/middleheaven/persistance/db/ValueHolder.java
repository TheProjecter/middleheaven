/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.db.metamodel.DBColumnModel;

/**
 * 
 */
public interface ValueHolder {

	/**
	 * 
	 * @param parameters parameters to obtain the value.
	 * @return the value.
	 */
	public Object getValue(QueryParameters parameters);

	/**
	 * @return the {@link DBColumnModel}
	 */
	public DBColumnModel getModel();
}
