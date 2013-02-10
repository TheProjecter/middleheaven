package org.middleheaven.persistance.db.metamodel;


/**
 * Represents the model in a RDBMS
 */
public interface DataBaseModel extends Iterable<DataBaseObjectModel> {

	public DataBaseObjectModel getDataBaseObjectModel(String name, DataBaseObjectType type);

}