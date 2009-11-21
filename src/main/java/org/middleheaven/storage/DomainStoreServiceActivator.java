package org.middleheaven.storage;

import org.middleheaven.core.wiring.BindConfiguration;
import org.middleheaven.core.wiring.Binder;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.core.wiring.activation.ActivationContext;
import org.middleheaven.core.wiring.activation.Activator;
import org.middleheaven.core.wiring.activation.Publish;
import org.middleheaven.core.wiring.annotations.Wire;

public class DomainStoreServiceActivator extends Activator {

	private DomainStoreService storeService;
	private WiringService wiringService;
	
	@Publish
	public DomainStoreService getDomainStoreService(){
		return storeService;
	} 
	
	@Wire
	public void setWiringService(WiringService wiringService){
		this.wiringService = wiringService;
	}
	
	@Override
	public void activate(ActivationContext context) {
		storeService = new DomainStoreService();
		
		// install an EntityStore provider
		
		wiringService.getObjectPool().addConfiguration( new BindConfiguration(){

			@Override
			public void configure(Binder binder) {
				Class cr = EntityStoreResolver.class;
				binder.bind(EntityStore.class).toResolver(cr);
			}
			
		});
	}

	@Override
	public void inactivate(ActivationContext context) {}
}