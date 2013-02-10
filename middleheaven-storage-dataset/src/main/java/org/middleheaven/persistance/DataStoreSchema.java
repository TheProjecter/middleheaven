/**
 * 
 */
package org.middleheaven.persistance;

import org.middleheaven.persistance.criteria.DataSetCriteria;
import org.middleheaven.sequence.Sequence;

/**
 * 
 */
public interface DataStoreSchema {

	/**
	 * Obtains a {@link DataQuery} for a given {@link DataSetCriteria}.
	 * Queries operate over all {@link DataSet}s in the store.
	 * 
	 * @param criteria
	 * @return
	 */
	public DataQuery query(DataSetCriteria criteria);

	
	/**
	 * Register a named query using a {@link NamedQueryExecutor} and a mnemonic name.
	 * @param name the menmonic name for the query
	 * @param queryExecutor the named query executor.
	 */
	public void registerNamedCriteria(String name, NamedQueryExecutor queryExecutor);
	
	public ParameterizedDataQuery namedQuery(String name);
	
	/**
	 * Obtains the required {@link DataSet}.
	 * 
	 * @param name
	 * @return
	 */
	public DataSet getDataSet(String name) throws DataSetNotFoundException;
	

	/**
	 * Changes the existing physical model according to the inner logic  model.
	 * 
	 * @param newModel the new model. 
	 * @throws ModelNotEditableException if this store does not accept model modifications.
	 */
	public void updateModel() throws ModelNotEditableException;
	
	
	/**
	 * @param dataSetName
	 */
	public Sequence<Long> getSequence(String sequenceName) throws SequenceNotFoundException;
	
	
	public boolean isReadable();
	
	public boolean isWritable();
	
}
