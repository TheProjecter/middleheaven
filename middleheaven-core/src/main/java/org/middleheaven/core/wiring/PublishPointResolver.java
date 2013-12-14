/**
 * 
 */
package org.middleheaven.core.wiring;

import org.middleheaven.reflection.ReflectedClass;

/**
 * 
 */
class PublishPointResolver implements Resolver {

	
	private ReflectedClass<?> publishingType;
	private PublishPoint publishPoint;
	
	public PublishPointResolver (PublishPoint publishPoint , ReflectedClass<?> publishingType){
		this.publishPoint = publishPoint;
		this.publishingType = publishingType;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object resolve(ResolutionContext context, WiringQuery query) {
		
		Object factory = context.getInstanceFactory().getInstance(WiringQuery.search(publishingType.getReflectedType()));
		
		return this.publishPoint.getObject(context.getInstanceFactory(), factory);
		
	}

}
