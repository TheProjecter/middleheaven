/*
 * Created on 2006/09/02
 *
 */
package org.middleheaven.core.bootstrap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.middleheaven.core.Container;
import org.middleheaven.core.ContextIdentifier;
import org.middleheaven.core.bootstrap.jboss.StandardJBossContainer;
import org.middleheaven.core.bootstrap.tomcat.TomcatContainer;
import org.middleheaven.logging.LoggingLevel;
import org.middleheaven.logging.ServletContextLogBookWriter;
import org.middleheaven.logging.WritableLogBook;

public class WebContainerBootstrap extends ExecutionEnvironmentBootstrap implements ServletContextListener{


	protected ServletContext context;

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		context.setAttribute("application.id", this.getContextIdentifier());
		context =  servletContextEvent.getServletContext();

		start(
				new WritableLogBook("web container book",LoggingLevel.ALL)
				.addWriter(new ServletContextLogBookWriter(context))
		);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		stop();
		context = null;
	}


	@Override
	public ContextIdentifier getContextIdentifier() {
		// get it from a parameter in the configuration
		return ContextIdentifier.getInstance((String)context.getInitParameter("application.id"));
	}

	@Override
	public Container getContainer() {
		// if there exists a property "jboss.server.home.url"
		// we are inside jboss; else assume we are in Tomcat only
		String jbossHome = System.getProperty("jboss.server.home.url");
		if (jbossHome==null){
			// can assume Tomcat ?
			String tomcatHome = System.getProperty("catalina.home");
			if (tomcatHome==null){
				// cannot
				throw new RuntimeException("Unsupported web container");
			} else {
				return new TomcatContainer(context);
			}
		} else {
			// inside JBoss
			return  new StandardJBossContainer(context);
		}
	}




}