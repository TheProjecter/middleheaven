package org.middleheaven.domain.model;


/**
 * Model builder for entities.
 * 
 */
public interface EntityModelBuildContext extends ModelSetItemBuilder<EditableDomainEntityModel> {

	/**
	 * 
	 * {@inheritDoc}
	 */
	public EditableDomainEntityModel getEditableModelOf(Class<?> type);

	public EditableEnumModel getEnumModel(Class enumType, Class persistableType);
	


}
