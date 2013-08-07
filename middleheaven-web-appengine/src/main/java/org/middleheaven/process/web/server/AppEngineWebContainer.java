/**
 * 
 */
package org.middleheaven.process.web.server;

import javax.servlet.ServletContext;

import org.middleheaven.core.services.Service;
import org.middleheaven.core.services.ServiceSpecification;
import org.middleheaven.io.repository.ManagedFileRepositoryProvider;
import org.middleheaven.process.web.CommonHttpServerContainers;
import org.middleheaven.util.Splitter;
import org.middleheaven.util.Version;
import org.middleheaven.web.container.WebContainerBootstrapEnvironment;
import org.middleheaven.web.container.WebContainerInfo;

/**
 * 
 */
public class AppEngineWebContainer extends WebContainerBootstrapEnvironment {

	/**
	 * Constructor.
	 * @param context
	 * @param root
	 */
	public AppEngineWebContainer(ServletContext context) {
		super(context);
		
//		String serverInfo = context.getServerInfo();
//
//		if (serverInfo.contains("Google App Engine")){
//			// it's App Engine
//			
//			/* ServletContext.getServerInfo() will return "Google App Engine Development/x.x.x"
//			* if will run locally, and "Google App Engine/x.x.x" if run on production envorinment */
//			
////			if (serverInfo.contains("Development")) {
////				// TODO  set profile to dev
////			} else {
////				// TODO set profile to production
////			}
//			
//		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "AppEngine";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WebContainerInfo getWebContainerInfo() {
		String serverInfo = this.getServletContext().getServerInfo();
	
		return new WebContainerInfo(CommonHttpServerContainers.APPENGINE , Version.valueOf(Splitter.on('/').split(serverInfo).getLast()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ManagedFileRepositoryProvider getManagedFileRepositoryProvider() {
		// determine appengine file system
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Service resolverRequestedService(ServiceSpecification spec) {
		return null;
	}


}
