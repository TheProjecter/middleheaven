/**
 * 
 */
package org.middleheaven.process.web.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.middleheaven.aas.AccessControlService;
import org.middleheaven.aas.StandardAccessControlService;
import org.middleheaven.core.bootstrap.AbstractBootstrap;
import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.BootstrapEnvironment;
import org.middleheaven.core.bootstrap.BootstrapEnvironmentResolver;
import org.middleheaven.core.services.ServiceBuilder;
import org.middleheaven.core.wiring.PropertiesPropertyManager;
import org.middleheaven.io.filecontext.FileContextService;
import org.middleheaven.logging.ConfigurableLogListener;
import org.middleheaven.logging.LoggingConfiguration;
import org.middleheaven.logging.LoggingConfigurator;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.ServletContextLogBookWriter;
import org.middleheaven.logging.WritableLogBook;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.web.service.WebUIServiceActivator;
import org.middleheaven.web.container.WebContainerBootstrapEnvironment;
import org.middleheaven.web.container.WebContainerInfo;
import org.middleheaven.web.container.WebContainerSwitcher;

/**
 * 
 */
public class WebContainerBootstrap extends AbstractBootstrap implements ServletContextListener{

	private ServletContext servletContext;
	private ServletHttpServerService httpService;

	public WebContainerBootstrap (){
	}

	protected ServletContext getServletContext(){
		return servletContext;
	}


	@Override
	protected final void doBeforeStart(BootstrapContext context){
		httpService = new ServletHttpServerService();	
		final AccessControlService accessControlService = new StandardAccessControlService();



		this.getRegistryServiceContext().register(AccessControlService.class, accessControlService, null);
		this.getRegistryServiceContext().register(HttpServerService.class, httpService, null);


		FileContextService fileContextService = context.getFileContextService();

		PropertiesPropertyManager mhEnvironment = PropertiesPropertyManager.loadFrom(
				fileContextService.getFileContext().getEnvironmentConfigRepository().retrive("mh_environment.config")
				);

		context.getPropertyManagers().addBefore("system.properties", mhEnvironment);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		//servlet hook

		servletContext =  servletContextEvent.getServletContext();

		servletContext.log("[MiddleHeaven] Initializing " + this.getClass().getSimpleName() + " at " + servletContext.getContextPath());


		try{

			//			start(
			//					new WritableLogBook("",LoggingLevel.ALL)
			//					.addWriter(new ServletContextLogBookWriter(servletContext))
			//			);

			// TODO (?) inicialize with ConfigurableListener refering the ServletContextLogBookWriter
			this.start();


			// hook for Frontend servlet
			this.servletContext.setAttribute("httpService", httpService);


			httpService.start();

			servletContext.log("[MiddleHeaven] Bootstap completed");
		}catch (Exception t){
			servletContext.log("[MiddleHeaven] Unexpected error", t);
		}
	}

	@Override
	protected void preConfig(BootstrapContext context) {
		// web application have a special initialization

		context.registerService(ServiceBuilder
				.forContract(UIService.class)
				.activatedBy(new WebUIServiceActivator())
				.newInstance()
				);


	}


	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stop();
		servletContext = null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getExecutionConfiguration() {
		return servletContext.getInitParameter("configuration");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BootstrapEnvironmentResolver bootstrapEnvironmentResolver() {
		return new WebContainerBootstrapEnvironmentResolver();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected LoggingConfigurator getStartUpLoggingConfigurator() {
		return new LoggingConfigurator(){

			@Override
			public void config(ConfigurableLogListener configurable, LoggingConfiguration configuration) {
				final WritableLogBook book = new WritableLogBook("",LoggingLevel.DEBUG)
				.addWriter(new ServletContextLogBookWriter(servletContext));
				
				configurable.addBook(book);
			}
			
		};
	}


	private class WebContainerBootstrapEnvironmentResolver implements BootstrapEnvironmentResolver {


		public WebContainerBootstrapEnvironmentResolver(){

		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public BootstrapEnvironment resolveEnvironment(BootstrapContext context) {
			WebContainerBootstrapEnvironment container = new WebContainerSwitcher().getWebContainer(getServletContext());

			getServletContext().setAttribute(WebContainerInfo.ATTRIBUTE_NAME, container.getWebContainerInfo());

			return container;
		}

	}



}
