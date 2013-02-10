/**
 * 
 */
package org.middleheaven.persistance.db;

import org.middleheaven.persistance.db.metamodel.DBColumnModel;

/**
 * 
 */
public class ExplicitValueHolder implements ValueHolder {

	
	private Object value;
	private DBColumnModel model;

	public ExplicitValueHolder (Object value, DBColumnModel model) {
		this.value = value;
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(QueryParameters parameters) {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DBColumnModel getModel() {
		return model;
	}
	
	public String toString(){
		return String.valueOf(value);
	}

}
