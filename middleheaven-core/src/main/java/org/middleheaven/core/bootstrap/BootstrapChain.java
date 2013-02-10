package org.middleheaven.core.bootstrap;

import java.util.List;

import org.middleheaven.logging.Logger;

public class BootstrapChain {

	List<BootstrapContainerExtention> extentions;
	private int current=0;
	private Logger logger;
	
	public BootstrapChain(Logger logger, List<BootstrapContainerExtention> extentions){
		this.extentions  = extentions;
		this.logger = logger;
	}
	
	
	public void doChain(BootstrapContext context) {
		if (current<extentions.size()){
			current++;

			final BootstrapContainerExtention item = extentions.get(current-1);
			try {
				// invoque 
				
				item.extend(context, this);
				
			} catch (Exception e){
				logger.error(e,"Exception found handling {0}", item.getClass());
			} catch (Error e){
				logger.fatal(e,"Exception found handling interceptor {0}" , item.getClass());

			}
			
		} 
	}

}
