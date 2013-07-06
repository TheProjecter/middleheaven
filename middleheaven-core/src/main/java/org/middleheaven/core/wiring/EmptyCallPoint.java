/**
 * 
 */
package org.middleheaven.core.wiring;



/**
 * 
 */
public class EmptyCallPoint implements PostCreatePoint , PreDestroiPoint{

	private static final EmptyCallPoint ME = new EmptyCallPoint();
	
	public static EmptyCallPoint getInstance(){
		return ME;
	}
	
	protected EmptyCallPoint (){
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void call(Object target) {
		//no-op
	}

}
