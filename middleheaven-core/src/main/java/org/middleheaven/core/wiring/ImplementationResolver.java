/**
 * 
 */
package org.middleheaven.core.wiring;

/**
 * Resolves an object delegating back to the factory
 */
public class ImplementationResolver implements Resolver {

	private DependendableBean implementationBean;

	/**
	 * Constructor.
	 * @param implementationBean
	 */
	public ImplementationResolver(DependendableBean implementationBean) {
		this.implementationBean = implementationBean;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		return context.getInstanceFactory().getInstance(WiringQuery.search(implementationBean.getType().getReflectedType()));
	}

}
