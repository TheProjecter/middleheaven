package org.middleheaven.persistance.db.datasource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.middleheaven.core.bootstrap.BootstapListener;
import org.middleheaven.core.bootstrap.BootstrapEvent;
import org.middleheaven.core.bootstrap.BootstrapService;
import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.persistance.DataStoreName;
import org.middleheaven.persistance.DataStoreProvider;
import org.middleheaven.persistance.db.DataSourceNameResolver;
import org.middleheaven.persistance.db.RDBMSDataStoreProvider;

/**
 * {@link DataSourceProvider} thaht embedes a HSQL implementation.
 */
public class EmbeddedDSProvider implements DataSourceProvider{


	/**
	 * 
	 * @param properties the connection properties
	 * 
	 * <ul>
	 * <li>datasource.url - the connection url</li>
	 * <li>datasource.username - the connection username</li>
	 * <li>datasource.password - the connection password</li>
	 * <li>datasource.catalog - the connection catalog (the database name)</li>
	 * <ul>
	 * 
	 * @return the {@link EmbeddedDSProvider} object.
	 */
	public static EmbeddedDSProvider provider(Properties properties){

		try {
			String  url = properties.getProperty("datasource.url");
			String username = properties.getProperty("datasource.username");
			String password = properties.getProperty("datasource.password");
			String catalog = properties.getProperty("datasource.catalog");
			return provider(catalog,new URL(url), username, password);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException(e);
		} 

	}

	/**
	 * Creates a in memory only volatile provider. The data stored where is deleted at 
	 * the of the application
	 * @param catalog the database name
	 * @return the {@link EmbeddedDSProvider} object.
	 */
	public static EmbeddedDSProvider provider(String catalog){
		return new EmbeddedDSProvider(new EmbeddedDataSource(catalog, "sa", ""));
	}
	
	/**
	 * Creates a in memory only volatile provider. The data stored where is deleted at 
	 * the of the application
	 * @param catalog the database name
	 * @param username the username
	 * @param password the password
	 * @return the {@link EmbeddedDSProvider} object.
	 */
	public static EmbeddedDSProvider provider(String catalog,String username,String password){
		return new EmbeddedDSProvider(new EmbeddedDataSource(catalog, username, password));
	}
	
	/**
	 * Creates a persisted data provider. The data stored where is storead in a file at the location of the URL.
	 * The URL must be writable from the application (normally a local disk URL).
	 * @param catalog the database name
	 * @param url the connection url.
	 * @return the {@link EmbeddedDSProvider} object.
	 */ 
	public static EmbeddedDSProvider provider(String catalog,URL url){
		return new EmbeddedDSProvider(new EmbeddedDataSource(url,catalog, "sa", ""));
	}
	
	/**
     * Creates a persisted data provider. The data stored where is storead in a file at the location of the URL.
	 * The URL must be writable from the application (normally a local disk URL).
	 * @param catalog the database name
	 * @param url the connection url.
	 * @param username the username
	 * @param password the password
	 * @return the {@link EmbeddedDSProvider} object.
	 */
	public static EmbeddedDSProvider provider(String catalog,URL url, String username,String password){
		return new EmbeddedDSProvider(new EmbeddedDataSource(url,catalog, username, password));
	}


	EmbeddedDataSource ds;
	private AtomicBoolean started = new AtomicBoolean();
	
	private EmbeddedDSProvider(EmbeddedDataSource ds) {
		this.ds = ds;
		ds.setAutoCommit(true);
		// register shutdownhook
		ServiceRegistry.getService(BootstrapService.class).addListener(new BootstapListener(){

			@Override
			public void onBoostapEvent(BootstrapEvent event) {
				if (event.isBootdown()){
					stop();
				}
			}
			
		});
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public DataSource getDataSource() {
		if (!started.get()){
			this.start();
		}
		return ds;
	}

	/**
	 * Start the provider.
	 */
	public void start(){
		started.set(true);
		ds.start();
	}

	/**
	 * Stop the provider
	 */
	public void stop(){
		started.set(false);
		ds.stop();
	}

	/**
	 * @return
	 */
	public DataSourceService getDataService() {
		return new OneProviderDataSourceService(this);
	}

	/**
	 * @return
	 */
	public DataStoreProvider getDataStoreProvider() {
		return new EmbededRDBMSDataStoreProvider(this.getDataService());
	}
	
	
	private static class EmbededRDBMSDataStoreProvider extends RDBMSDataStoreProvider {

		/**
		 * Constructor.
		 * @param dsService
		 * @param dataSourceNameResolver
		 */
		protected EmbededRDBMSDataStoreProvider(DataSourceService dsService) {
			super(dsService, new DataSourceNameResolver(){

				@Override
				public String resolveDataSourceName(DataStoreName name) {
					throw new UnsupportedOperationException("Not implememented yet");
				}
				
			});
		}
		
		
	}
}
