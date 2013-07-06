/*
 * Created on 2006/08/19
 *
 */
package org.middleheaven.core.bootstrap;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.application.ApplicationScanningService;
import org.middleheaven.application.StandardApplicationScannerServiceActivator;
import org.middleheaven.application.StandardApplicationServiceActivator;
import org.middleheaven.core.FileContext;
import org.middleheaven.core.FileContextService;
import org.middleheaven.core.bootstrap.activation.ActivatorDependencyResolver;
import org.middleheaven.core.bootstrap.activation.AnnotationBasedDependencyResolver;
import org.middleheaven.core.dependency.DependencyGraph;
import org.middleheaven.core.dependency.DependencyGraphEdge;
import org.middleheaven.core.dependency.DependencyGraphNode;
import org.middleheaven.core.dependency.DependencyInicilizer;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceActivator;
import org.middleheaven.core.services.ServiceBuilder;
import org.middleheaven.core.services.ServiceContext;
import org.middleheaven.core.services.ServiceNotAvailableException;
import org.middleheaven.core.services.ServiceProvider;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.ProfilesBag;
import org.middleheaven.core.wiring.PropertyManagers;
import org.middleheaven.core.wiring.StandardWiringService;
import org.middleheaven.core.wiring.SytemEnvPropertyManager;
import org.middleheaven.core.wiring.SytemPropertyManager;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.global.text.CultureModelLocalizationService;
import org.middleheaven.graph.Graph;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.PropertiesFile;
import org.middleheaven.logging.BroadcastLoggingService;
import org.middleheaven.logging.ConfigurableLogListener;
import org.middleheaven.logging.LogServiceDelegatorLogger;
import org.middleheaven.logging.Logger;
import org.middleheaven.logging.LoggingConfiguration;
import org.middleheaven.logging.LoggingConfigurator;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.logging.XMLLoggingConfigurator;
import org.middleheaven.util.Splitter;
import org.middleheaven.util.StopWatch;

/**
 * Implements the generic inicilaization engine for the MiddleHeaven platform.
 * 
 * Subclasses of {@link AbstractBootstrap} implement bootstrap in different
 * execution environments and enable for the application's execution to be
 * environment independent.
 * 
 * Every bootstrap defines implementations for a set of pre-defined services.
 * The provided services are:
 * 
 * <ul>
 * <li>{@link LoggingService} - Provides the generic mecanics to deliver logging
 * messages</li>
 * <ul>
 * 
 */
public abstract class AbstractBootstrap {


	private final PropertyManagers propertyManagers = new PropertyManagers();
	private final Set<ActivatorDependencyResolver> resolvers = new CopyOnWriteArraySet<ActivatorDependencyResolver>();

	private BootstrapEnvironmentResolver bootstrapEnvironmentResolver;
	private SimpleBootstrapService bootstrapService;
	private RegistryServiceContext serviceRegistryContext;
	private LoggingService loggingService;
	private WiringService wiringService;
	private Logger logger;
	private BootstrapEnvironment environment;
	private BootstrapContext context;

	public AbstractBootstrap() {
		this.context = null;

		serviceRegistryContext = new RegistryServiceContext(); //RegistryServiceContext.getInstance();
		ServiceRegistry.context = serviceRegistryContext;

		// prepare properties.
		this.propertyManagers.addFirst(new SytemEnvPropertyManager());
		this.propertyManagers.addFirst(new SytemPropertyManager());

		this.resolvers.add(new AnnotationBasedDependencyResolver());

		this.wiringService = new StandardWiringService(this.propertyManagers, this.serviceRegistryContext);
	}

	protected WiringService getWiringService() {
		return this.wiringService;
	}

	public void setWiringService(WiringService wiringService) {
		this.wiringService = wiringService;
	}

	protected final RegistryServiceContext getRegistryServiceContext() {
		return serviceRegistryContext;
	}

	protected LoggingService resolveLoggingService() {
		return new BroadcastLoggingService();
	}

	protected LocalizationService resolveLocalizationService() {
		return new CultureModelLocalizationService();
	}

	protected LoggingService getLoggingService() {
		return loggingService;
	}

	protected BootstrapEnvironment getContainer() {
		return this.environment;
	}

	protected abstract String getExecutionConfiguration();

	public void addActivatorDependencyResolver(
			ActivatorDependencyResolver listener) {
		resolvers.add(listener);
	}

	public void removeActivatorDependencyResolver(
			ActivatorDependencyResolver listener) {
		resolvers.remove(listener);
	}

	/**
	 * 
	 * @return the {@link BootstrapEnvironmentResolver} implementations to use.
	 */
	protected abstract BootstrapEnvironmentResolver bootstrapEnvironmentResolver();

	/**
	 * Start the environment
	 */
	protected final void start() {

		StopWatch watch = StopWatch.start();

		// Phase 1 - default services

		// set Logging Service
		this.loggingService = this.resolveLoggingService();

		// Register Logging Service
		serviceRegistryContext.register(LoggingService.class, loggingService);

		// set default logger
		this.logger = new LogServiceDelegatorLogger(this.getClass().getName(), loggingService);

		LoggingConfigurator temporaryConfigurator = this.getStartUpLoggingConfigurator();

		ConfigurableLogListener listener = new ConfigurableLogListener(new LoggingConfiguration(null), temporaryConfigurator);
		this.loggingService.addLogListener(listener);

		serviceRegistryContext.register(LocalizationService.class, resolveLocalizationService());

		logger.info("Begin MiddleHeaven bootstrap");

		// set the environment resolver
		this.bootstrapEnvironmentResolver = this.bootstrapEnvironmentResolver();

		// Determine Bootstrap Service
		bootstrapService = new SimpleBootstrapService(this);

		bootstrapService.addListener(new WiringBootstrapListener( this.wiringService, this.serviceRegistryContext));

		// Register Bootstrap Service
		serviceRegistryContext.register(BootstrapService.class,
				bootstrapService);
		serviceRegistryContext.register(WiringService.class, wiringService);


		final Collection<Service> services = new LinkedHashSet<Service>();

		context = new DefaultBootstrapContext(this, services);
		// Resolve Container
		// Phase 2 - enviroment

		this.environment = bootstrapEnvironmentResolver.resolveEnvironment(context);

		logger.info("Using environment: {0}", environment.getName());

		// Resolve FileSystem
		FileContext fileSystem = environment.getFileContext();
		
		serviceRegistryContext.register(FileContextService.class, new FileSystemFileContextService(fileSystem));
		
		wiringService.addConfiguration( new FileContextBindConfiguration(fileSystem));

		// configure permanent logging
		this.loggingService.removeAllListeners();
		listener = new ConfigurableLogListener(new LoggingConfiguration(null), this.getPermanentLoggingConfigurator());
		this.loggingService.addLogListener(listener);

		doBeforeStart(context);

		// apply profiles to wiring service
		logger.trace("Configuring active environment profiles");

		String profiles = propertyManagers.getProperty(
				"middleheaven.profiles.active", String.class);

		if (profiles != null) {
			wiringService.getActiveProfiles().add(Splitter.on(',').trim().split(profiles).into(new LinkedList<String>()));
		}

		logger.debug("Register environment services");

		// activate services that can be overridden by the container or final
		// environment

		logger.debug("Scanning for extentions");

		List<BootstrapContainerExtention> extentions = new ArrayList<BootstrapContainerExtention>();

		readExtentions(extentions);

		// configurate container with chain of extentions
		BootstrapChain chain = new BootstrapChain(logger, extentions);

		logger.debug("Configuring context");

		logger.debug("Inicialize service discovery");

		environment.preConfigurate(context);

		// config any specific environment activators
		preConfig(context);

		// start bootstrap
		// any participant that wishes to add activators can do so now.
		bootstrapService.fireBootupStart();

		// configure using extentions
		chain.doChain(context);

		// call configuration
		posConfig(context);

		// at this point all needed activators are collected.

		// scan and activate all services
		activate(services, serviceRegistryContext);

		// Phase 3 - inicilize modules
		// ApplicationModulesResolver modulesResolver =
		// environment.getApplicationModulesResolver();

		// modulesResolver.resolveApplicationModules();

		// TODO usar applicationScanner
		StandardApplicationServiceActivator act = new StandardApplicationServiceActivator();

		act.activate(serviceRegistryContext);

		StandardApplicationScannerServiceActivator scannerServiceActivator = new StandardApplicationScannerServiceActivator();

		scannerServiceActivator.activate(serviceRegistryContext);

		serviceRegistryContext.getService(ApplicationScanningService.class).scan();

		//
		wiringService.refresh();

		doAfterStart(context);

		environment.posConfigurate(context);

		// inform the end of the bootstrap
		bootstrapService.fireBootupEnd();

		logger.info("MiddleHeaven bootstraped in {0} ms. ", watch.mark().milliseconds());

		logger.info("Starting environment");

		// start the environment. the ui is available.
		environment.start();

	}


	/**
	 * Obtains a {@link LoggingConfigurator} for the begining of the bootstrap sequence before the proper basic services are bootstrapped.
	 * Normally this configures some enviroment specific logging bridge. After the basic services are bootstrapped this configurations e abandoned and 
	 * the final configuratios is obtained from   {@link #getPermanentLoggingConfigurator()}.
	 * 
	 * @see {@link #getPermanentLoggingConfigurator()}
	 * @return the start up configurations to be used temporalily.
	 */
	protected abstract LoggingConfigurator getStartUpLoggingConfigurator();

	/**
	 * Obtains a {@link LoggingConfigurator} for the enviroment.
	 * 
	 * @see {@link #getPermanentLoggingConfigurator()}
	 * @return the start up configurations to be used permantly.
	 */
	protected LoggingConfigurator getPermanentLoggingConfigurator() {
		final FileContext fileRepositoryService = wiringService.getInstance(FileContextService.class).getFileContext();

		ManagedFile configFolder = fileRepositoryService.getEnvironmentConfigRepository();

		if (configFolder != null && configFolder.exists() && configFolder.isReadable()){
			ManagedFile configXML = configFolder.retrive("log-config.xml");
			if (configXML!=null && configXML.exists() && configXML.isReadable()){
				try {
					return new XMLLoggingConfigurator(configXML.getURI().toURL());
				} catch (MalformedURLException e) {
					throw new IllegalArgumentException(e);
				}

			}
		} 

		// if no file is defined continue to use the startup configuration.
		return getStartUpLoggingConfigurator();

	}





	/**
	 * activate all collected services
	 * 
	 * @param services
	 *            the services to activate
	 * @param serviceContext
	 *            the context to activate them with.
	 */
	protected void activate(Collection<Service> allServices,
			final ServiceContext serviceContext) {


		List<Service> services = new LinkedList<Service>(allServices);

		// add the services already provided by the bootstrap with null
		// activator.

		ServiceActivator doNothingActivator = new DoNothingServiceActivator();

		services.add(ServiceBuilder.forContract(BootstrapService.class)
				.activatedBy(doNothingActivator).newInstance());
		services.add(ServiceBuilder.forContract(LoggingService.class)
				.activatedBy(doNothingActivator).newInstance());
		services.add(ServiceBuilder.forContract(FileContextService.class)
				.activatedBy(doNothingActivator).newInstance());
		services.add(ServiceBuilder.forContract(LocalizationService.class)
				.activatedBy(doNothingActivator).newInstance());
		services.add(ServiceBuilder.forContract(WiringService.class)
				.activatedBy(doNothingActivator).newInstance());


		Map<ServiceSpecification, Service> servicesMap = new HashMap<ServiceSpecification, Service>();
		Map<Service, DependencyGraph> serviceGraphMapping = new HashMap<Service, DependencyGraph>();

		// add all services specifications targets to map and inicialize the seed graphs
		for (Service s : services) {
			servicesMap.put(s.getServiceSpecification(), s);

			if (s.getDependencies().isEmpty()) {
				// is seed
				serviceGraphMapping.put(s, new DependencyGraph());
			}
		}

		DependencyInicilizer ini = new ServiceDependencyInicilizer(serviceContext);
		// Create DependencyGraph

		for (Service s : services) {
			for (ServiceSpecification dependencySpecification : s.getDependencies()) {

				Service dependencyService = servicesMap.get(dependencySpecification);

				DependencyGraph targetGraph = serviceGraphMapping.get(dependencyService);
				DependencyGraph serviceGraph = serviceGraphMapping.get(s);


				final DependencyGraphNode dependency = new DependencyGraphNode(dependencyService, ini);
				if (targetGraph == null){
					for (DependencyGraph g : serviceGraphMapping.values()){
						for (Graph.Vertex<DependencyGraphNode,DependencyGraphEdge> vertex : g.getVertices()){
							if (vertex.getObject().equals(dependency)){
								// this is the graph

								if (serviceGraph != null && !serviceGraph.equals(g)) {
									// need merge
									merge(serviceGraphMapping, ini, s,
											dependencySpecification, dependencyService,
											targetGraph, serviceGraph, dependency);
								} else {
									g.addDependency(dependency, new DependencyGraphNode(s, ini), !dependencySpecification.isOptional());
									serviceGraphMapping.put(s, g);
								}

							}
						}
					}
				} else {
					if (serviceGraph != null && !serviceGraph.equals(targetGraph)) {
						// need merge

						merge(serviceGraphMapping, ini, s,
								dependencySpecification, dependencyService,
								targetGraph, serviceGraph, dependency);

					} else {
						targetGraph.addDependency(
								dependency,
								new DependencyGraphNode(s, ini), !dependencySpecification.isOptional()
								);
						serviceGraphMapping.put(s, targetGraph);
					}
				}

			}
		}


		for (Map.Entry<Service, DependencyGraph> entry : serviceGraphMapping.entrySet()){
			DependencyGraph graph = entry.getValue();
			if (graph.isEmpty()){
				// there is no vertice in the graph so inicilize the mapped service directly
				ini.inicialize(new DependencyGraphNode(entry.getKey(), ini));
			} else {
				graph.resolve();
			}
		}

		for (Iterator<Service> it = services.iterator(); it.hasNext();) {
			final Service service = it.next();
			if (!service.isActivated()){
				throw new IllegalStateException("Service " + service.getName() + " was not inicilized." );
			} 
		}



	}



	private void merge(Map<Service, DependencyGraph> serviceGraphMapping,
			DependencyInicilizer inicializer, Service service,
			ServiceSpecification dependencySpecification,
			Service dependencyService, DependencyGraph targetGraph,
			DependencyGraph serviceGraph, final DependencyGraphNode dependency) {
		if (targetGraph.isEmpty()){
			serviceGraph.addDependency(
					dependency,
					new DependencyGraphNode(service, inicializer), !dependencySpecification.isOptional()
					);
			serviceGraphMapping.put(dependencyService, serviceGraph);

		} else {
			DependencyGraph mergedGraph = serviceGraph.merge(targetGraph);

			List<Service> keyServices = new LinkedList<Service>();

			for (Map.Entry<Service, DependencyGraph> entry : serviceGraphMapping.entrySet()){
				if (entry.getValue().equals(targetGraph) || entry.getValue().equals(serviceGraph)){
					keyServices.add(entry.getKey());
				}
			}

			serviceGraphMapping.values().remove(serviceGraph);
			serviceGraphMapping.values().remove(targetGraph);

			for (Service sk : keyServices){
				serviceGraphMapping.put(sk, mergedGraph);
			}
		}
	}

	protected void readExtentions(List<BootstrapContainerExtention> extentions) {

		ManagedFile file = this.getContainer().getFileContext()
				.getAppConfigRepository().retrive("mh-bootstrap.config");

		if (file.exists()) {
			Properties p = PropertiesFile.from(file).toProperties();

			for (String extention : Splitter.on(",").split(p.getProperty("middleheaven.bootstrap.extentions"))) {
				extentions.add(Introspector
						.of(BootstrapContainerExtention.class).load(extention)
						.newInstance());
			}
		}

	}

	protected void preConfig(BootstrapContext context) {
		// no-op
	}

	public void posConfig(BootstrapContext context) {

	}

	public final void stop() {
		logger.info("Terminating Environment");
		bootstrapService.fireBootdownStart();
		doBeforeStop(context);

		serviceRegistryContext.unRegister(BootstrapService.class);
		serviceRegistryContext.unRegister(WiringService.class);

		// TODO unregister all

		doAfterStop(context);

		bootstrapService.fireBootdownEnd();
		logger.info("Environment terminated");
	}

	protected void doBeforeStart(BootstrapContext context) {
	};

	protected void doAfterStart(BootstrapContext context) {
	};

	protected void doBeforeStop(BootstrapContext context) {
	};

	protected void doAfterStop(BootstrapContext context) {
	}

	private static class ServiceDependencyInicilizer implements DependencyInicilizer {

		
		private ServiceContext serviceContext;
		public ServiceDependencyInicilizer (ServiceContext serviceContext){
			this.serviceContext = serviceContext;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void inicialize(DependencyGraphNode node) {
			Service s = (Service) node.getObject();

			if (!s.isActivated()) {
				s.activate(serviceContext);
			}
		}
		
	}
	
	private static class DoNothingServiceActivator extends ServiceActivator {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void collectRequiredServicesSpecifications(
				Collection<ServiceSpecification> specs) {
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void collectPublishedServicesSpecifications(
				Collection<ServiceSpecification> specs) {
		
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void activate(ServiceContext serviceContext) {
		
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void inactivate(ServiceContext serviceContext) {
			
		}
		
	}
	
	private static class FileContextBindConfiguration implements BindConfiguration {

		private FileContext fileSystem;
		
		public FileContextBindConfiguration (FileContext fileSystem){
			this.fileSystem = fileSystem;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void configure(Binder binder) {
			binder.bind(FileContext.class).inSharedScope().toInstance(fileSystem);
		}
		
	}

	private static class SimpleBootstrapService implements BootstrapService {

		private AbstractBootstrap executionEnvironmentBootstrap;
		private Set<BootstapListener> listeners = new CopyOnWriteArraySet<BootstapListener>();

		public SimpleBootstrapService(
				AbstractBootstrap executionEnvironmentBootstrap) {
			this.executionEnvironmentBootstrap = executionEnvironmentBootstrap;
		}

		protected void fireBootupStart() {
			fire(new BootstrapEvent(true, true));
		}

		protected void fireBootupEnd() {
			fire(new BootstrapEvent(true, false));
		}

		protected void fireBootdownEnd() {
			fire(new BootstrapEvent(false, false));
		}

		protected void fireBootdownStart() {
			fire(new BootstrapEvent(false, true));
		}

		private void fire(BootstrapEvent event) {
			for (BootstapListener listener : listeners) {
				listener.onBoostapEvent(event);
			}
		}

		@Override
		public void addListener(BootstapListener listener) {
			listeners.add(listener);
		}

		@Override
		public void removeListener(BootstapListener listener) {
			listeners.remove(listener);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void stop() {
			executionEnvironmentBootstrap.stop();
		}

	}

	private static class WiringBootstrapListener implements BootstapListener{

		private WiringService wiringService;
		private RegistryServiceContext serviceRegistryContext;


		public WiringBootstrapListener(WiringService wiringService,
				RegistryServiceContext serviceRegistryContext) {
			super();
			this.wiringService = wiringService;
			this.serviceRegistryContext = serviceRegistryContext;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onBoostapEvent(BootstrapEvent event) {
			if (event.isBootdown()) {
				wiringService.close();
				serviceRegistryContext.clear();
			}
		}

	}

	private static class FileSystemFileContextService implements FileContextService {

		
		private FileContext fileSystem;
		public FileSystemFileContextService( FileContext fileSystem){
			this.fileSystem = fileSystem;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public FileContext getFileContext() {
			return fileSystem;
		}	
		
	}
	
	private static class DefaultBootstrapContext implements BootstrapContext {
		
		final Collection<Service> services;
		private AbstractBootstrap bootstrap;
		
		public DefaultBootstrapContext (AbstractBootstrap bootstrap , Collection<Service> services){
			this.services = services;
			this.bootstrap = bootstrap;
		}

		public String getName() {
			return bootstrap.getExecutionConfiguration();
		}

		@Override
		public BootstrapContext registerService(Service service) {
			services.add(service);
			return this;
		}

		@Override
		public BootstrapContext removeService(Service service) {
			services.remove(service);
			return this;
		}

		@Override
		public boolean isRegistered(Service service) {
			return services.contains(service);
		}

		@Override
		public ProfilesBag getActiveProfiles() {
			return bootstrap.wiringService.getActiveProfiles();
		}

		@Override
		public PropertyManagers getPropertyManagers() {
			return bootstrap.propertyManagers;
		}

		@Override
		public LoggingService getLoggingService() {
			return bootstrap.loggingService;
		}

		@Override
		public FileContextService getFileContextService() {
			return bootstrap.serviceRegistryContext
					.getService(FileContextService.class);
		}

		@Override
		public BootstrapContext requireService(ServiceSpecification spec) throws ServiceNotAvailableException {
			internalRequireService(spec, services, null);
			return this;
		}

		@Override
		public BootstrapContext requireService(ServiceSpecification spec, ServiceProvider provider) {
			internalRequireService(spec, services, provider);
			return this;
		}

		private void internalRequireService(ServiceSpecification spec, Collection<Service> servicePool, ServiceProvider provider){

			if (specIsIn(spec, servicePool)){
				return;
			}

			// check for native provider
			Service service = null;
			try {
				service = bootstrap.getContainer().provideService(spec);

			} catch (ServiceNotAvailableException e){
				// log ?
			}

			if (service == null && provider != null ){
				service = provider.provide(spec);
			}

			if (service == null){
				throw new ServiceNotAvailableException(spec.getServiceContractType().getName());
			}

			servicePool.add(service);
		}


		private boolean specIsIn(ServiceSpecification spec, Collection<Service> servicePool){

			for (Service s : servicePool){
				if (s.getServiceSpecification().matches(spec)){
					return true;
				}
			}
			return false;
		}
	}
}
