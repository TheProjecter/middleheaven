/**
 * 
 */
package org.middleheaven.persistance.db;

import java.util.Collection;

import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.persistance.DataService;
import org.middleheaven.persistance.db.datasource.DataSourceService;

/**
 * 
 */
public class JDBCDataBaseServiceActivator extends ServiceActivator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectRequiredServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(DataSourceService.class));
		specs.add(ServiceSpecification.forService(DataService.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void collectPublishedServicesSpecifications(Collection<ServiceSpecification> specs) {
		specs.add(ServiceSpecification.forService(DataBaseService.class));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void activate(ServiceContext serviceContext) {
		
		DataSourceService dataSourceService = serviceContext.getService(DataSourceService.class);
		DataService dataService = serviceContext.getService(DataService.class);
		
		final JDBCDataBaseService implementation = new JDBCDataBaseService(dataSourceService);
		
		dataService.addProvider(implementation.getDataStoreProvider());

		
		serviceContext.register(DataBaseService.class, implementation);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inactivate(ServiceContext serviceContext) {
	
	}

}
