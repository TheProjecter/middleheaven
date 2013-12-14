package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.reflection.inspection.ClassIntrospector;

/**
 * Determines the container from the current {@link ServletContext}.
 */
public class WebContainerSwitcher {

	/**
	 * Determines the container from the current {@link ServletContext}.
	 * @param servletContext
	 * @param rootFolder 
	 * @return
	 */
	public WebContainerBootstrapEnvironment getWebContainer(ServletContext servletContext){
		
	
		String serverInfo = servletContext.getServerInfo();
		
		if (serverInfo.contains("Google App Engine")){
			// it's App Engine
			
			final String className = "org.middleheaven.process.web.server.AppEngineWebContainer";
			if (ClassIntrospector.isInClasspath(className)){
				return ClassIntrospector.of(WebContainerBootstrapEnvironment.class).load(className).newInstance(servletContext);
			} else {
				// TODO warning that the corret container was not found
				return new StandardSevletBootstrapEnvironment(servletContext);
			}
			
			
		} else {

			// if there exists a property "jboss.server.home.url" we are inside jboss
			String jbossHome = System.getProperty("jboss.server.home.url");
		
			if (jbossHome==null){
				// can assume Tomcat ?
				String tomcatHome = System.getProperty("catalina.home");
				if (tomcatHome==null){
					// cannot
					return new StandardSevletBootstrapEnvironment(servletContext);
				} else {
					return new CatalinaContainerBootstrapEnvironment(servletContext);
				}
			} else {
				// inside JBoss
				return  new StandardJBossBootstrapEnvironment(servletContext);
			}
		}

		
		
	}
}
