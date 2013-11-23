package org.middleheaven.persistance.db.metamodel;

import java.util.Collection;

import org.middleheaven.collections.enumerable.Enumerable;

public interface DBTableModel extends Iterable<DBColumnModel>{

	public abstract String getName();

	public abstract ColumnModelGroup identityColumns();

	public abstract Collection<ColumnModelGroup> uniqueColumns();

	public abstract Collection<ColumnModelGroup> indexedColumns();

	public abstract Enumerable<DBColumnModel> columns();

	/**
	 * @return
	 */
	public abstract boolean isEmpty();


}