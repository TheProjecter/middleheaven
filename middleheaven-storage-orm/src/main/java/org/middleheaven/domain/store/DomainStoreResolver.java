/**
 * 
 */
package org.middleheaven.domain.store;

import org.middleheaven.core.wiring.ResolutionContext;
import org.middleheaven.core.wiring.Resolver;
import org.middleheaven.core.wiring.WiringQuery;

/**
 * Resolver {@link DomainStore}s for wiring.
 */
class DomainStoreResolver implements Resolver {

	
	private DomainStoreService domainStoreService;

	public DomainStoreResolver(DomainStoreService domainStoreService){
		this.domainStoreService = domainStoreService;
	}
	
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		
		final Object name = query.getParam("name");
		return new NamedEntityStore(name == null ? null : name.toString());
	}
	
	
	public class NamedEntityStore extends AbstractDomainStoreDecorator{
		
		private String name;

		public NamedEntityStore(String name){
			this.name = name;
		}
		
		@Override
		protected DomainStore original() {
			return (name==null) ? domainStoreService.getStore() : domainStoreService.getStore(name);	
		}

	}
}