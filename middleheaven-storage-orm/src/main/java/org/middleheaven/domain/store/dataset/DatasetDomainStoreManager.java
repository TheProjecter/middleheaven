/**
 * 
 */
package org.middleheaven.domain.store.dataset;

import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.store.AbstractEntityInstanceStoreManager;
import org.middleheaven.domain.store.Query;
import org.middleheaven.domain.store.mapping.DataSetEntityInstanceStorage;
import org.middleheaven.domain.store.mapping.DomainModelDataSetTypeMapper;
import org.middleheaven.persistance.DataService;

/**
 * 
 */
public class DatasetDomainStoreManager extends AbstractEntityInstanceStoreManager {

	public static DatasetDomainStoreManager manage(DataService dataService, DomainModel domainModel, DomainModelDataSetTypeMapper dmFunction){

		DataSetEntityInstanceStorage objectStorage = new DataSetEntityInstanceStorage(dataService, domainModel , dmFunction);

		
		return new DatasetDomainStoreManager(objectStorage, domainModel);
	}
	
	/**
	 * Constructor.
	 * @param identityManager
	 * @param storage
	 * @param domainModel
	 */
	protected DatasetDomainStoreManager(DataSetEntityInstanceStorage objectStorage, DomainModel domainModel) {
		super(objectStorage.getIdentityManager(), objectStorage, domainModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> Query<T> retriveNameQuery(String name, Class<T> type) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

}
