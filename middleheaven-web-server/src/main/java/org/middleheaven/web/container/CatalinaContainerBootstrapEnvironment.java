/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.web.container;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletContext;

import org.middleheaven.core.bootstrap.BootstrapContext;
import org.middleheaven.core.bootstrap.EditableContainerFileRepositoryManager;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.services.ServiceBuilder;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.io.repository.machine.MachineFiles;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.LoggingService;
import org.middleheaven.logging.ServletContextLogListener;
import org.middleheaven.namedirectory.NameDirectoryService;
import org.middleheaven.namedirectory.jndi.JNDINamingDirectoryActivator;
import org.middleheaven.process.web.CommonHttpServerContainers;
import org.middleheaven.util.PropertiesBag;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.Version;

/**
 * 
 * @see WebContainerBootstrapEnvironment
 */
public class CatalinaContainerBootstrapEnvironment extends StandardSevletBootstrapEnvironment  {


	public CatalinaContainerBootstrapEnvironment(ServletContext context){
		super(context);
	}

	@Override
	public void configurate(BootstrapContext context) {
		super.configurate(context);
		
		Boolean useNaming = context.getPropertyManagers().getProperty("catalina.useNaming", Boolean.class, Boolean.FALSE);
		
		if(useNaming.booleanValue()){
			context.registerService(ServiceBuilder
					.forContract(NameDirectoryService.class)
					.activatedBy(new JNDINamingDirectoryActivator())
					.newInstance()
		    );
		}

		final ServletContext servletContext = this.getServletContext();
		final ServletContextLogListener listener = new ServletContextLogListener(servletContext);
		
		String level = servletContext.getInitParameter("logging.level");
		
		if (!StringUtils.isEmptyOrBlank(level)){
			listener.setLevel(LoggingLevel.valueOf(level.toUpperCase()));
		} else {
			listener.setLevel(LoggingLevel.WARN);
		}
		
		context.getLoggingService().addLogListener(listener);
	}



	
	@Override
	protected void setupDefaultFilesRepositories(ServletContext context,EditableContainerFileRepositoryManager fileSystem){
	
		String catalinaBasePath = PropertiesBag.bagOfSystemProperties().getProperty("catalina.base", String.class);
		
		File catalinaBase = new File(catalinaBasePath);

		fileSystem.setEnvironmentConfigRepository(MachineFiles.resolveFile(new File(catalinaBase ,  "conf")));
		fileSystem.setEnvironmentDataRepository(MachineFiles.resolveFile(new File(catalinaBase , "data")).createFolder());

		super.setupDefaultFilesRepositories(context, fileSystem);
		
	}

	@Override
	public String getName() {
		return CommonHttpServerContainers.TOMCAT.name();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebContainerInfo getWebContainerInfo() {
		ClassIntrospector<?> introspector = ClassIntrospector.loadFrom("org.apache.catalina.util.ServerInfo");
		try {
			String number = (String) introspector.inspect().methods().beingStatic(true).named("getServerNumber").retrive().invoke(null, new Object[0]);
			String serverBuilt = (String) introspector.inspect().methods().beingStatic(true).named("getServerBuilt").retrive().invoke(null, new Object[0]);

			number = number  + "." + serverBuilt;
			
			return new WebContainerInfo(CommonHttpServerContainers.TOMCAT, Version.valueOf(number));
			
		} catch (IllegalArgumentException e) {
			throw ReflectionException.manage(e, introspector.getIntrospected());
		} catch (IllegalAccessException e) {
			throw ReflectionException.manage(e, introspector.getIntrospected());
		} catch (InvocationTargetException e) {
			throw ReflectionException.manage(e, introspector.getIntrospected());
		}
	}







}
