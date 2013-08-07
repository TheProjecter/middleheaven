package org.middleheaven.persistance.db.datasource;

import java.util.Properties;

import javax.sql.DataSource;

import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.NamingDirectoryException;
import org.middleheaven.persistance.PersistanceException;

/**
 * Provides DataSource objects from a location in a NameDirectoryService
 */
final class NameDirectoryLookupDSProvider implements DataSourceProvider{

	private NameDirectoryService service;
	private String url;

	public static NameDirectoryLookupDSProvider provider(NameDirectoryService service, String url){
		return new NameDirectoryLookupDSProvider(service,url);
	}
	
	public static NameDirectoryLookupDSProvider provider(NameDirectoryService service, Properties properties){
		String  url = properties.getProperty("datasource.url");
		url = url.substring(url.indexOf(':')+1);

		return provider(service,url);
	}

	private NameDirectoryLookupDSProvider(NameDirectoryService service,String url){
		this.service = service;
		this.url = url;
	}


	@Override
	public DataSource getDataSource() {
		try {
			return service.lookup(url,DataSource.class);
		} catch (NamingDirectoryException e) {
			throw new PersistanceException(e);
		}
	}


}
