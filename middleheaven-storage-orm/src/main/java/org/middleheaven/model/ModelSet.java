package org.middleheaven.model;

import org.middleheaven.collections.enumerable.Enumerable;

/**
 * The set of models, mapped by modeled classs.
 * @param <M>
 */
public interface ModelSet<M> {

	
	/**
	 * 
	 * @param <T> the model type
	 * @return and Enumerable  with all the EntityModel of the domain.
	 */
	public  Enumerable<M> models();	

	/**
	 * 
	 * @param entityName the modeled class entity name.
	 * @return the corresponding model.
	 */
	public M getModelFor(String entityName);
	
	
	/**
	 * @param name
	 */
	boolean containsModelFor(String name);
	
}
