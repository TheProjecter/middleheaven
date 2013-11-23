package org.middleheaven.model;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.collections.enumerable.Enumerables;

/**
 * 
 * @param <M> the model type
 */
public abstract class AbstractEditableModelSet<M> implements ModelSet<M> {


	private final Map<String, M> models = new HashMap<String, M>();

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Enumerable<M> models() {
		return  Enumerables.asEnumerable(models.values());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public M getModelFor(String entityName) {
		return models.get(entityName);
	}

	/**
	 * 
	 * @param <E> the modeled class type
	 * @param entityName the modeled entity name
	 * @param model the model for the entity.
	 */
	public <E> void addModel(String entityName, M model) {
		models.put(entityName, model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsModelFor(String name) {
		return models.containsKey(name);
	}

}
