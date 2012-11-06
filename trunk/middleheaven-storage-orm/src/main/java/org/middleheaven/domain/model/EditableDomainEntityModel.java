package org.middleheaven.domain.model;

import org.middleheaven.core.reflection.metaclass.MetaClass;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.collections.Enumerable;

/**
 * An editable {@link EntityModel}.
 */
public interface EditableDomainEntityModel extends EntityModel {
	
	/**
	 * 
	 * @param logicName the field's logic name
	 * @return the field entity model
	 */
	public EditableEntityFieldModel fieldModel(QualifiedName logicName);
	
	/**
	 * 
	 * @return the identity field entity model
	 */
	public EditableEntityFieldModel identityFieldModel();

	
	/**
	 * 
	 * @return all entity's field entity model.
	 */
	public Enumerable<? extends EditableEntityFieldModel> fields();

	/**
	 * 
	 * @param identityType the {@link MetaClass} for the identityType.
	 */
	public void setIdentityType(MetaClass identityType);

	/**
	 * @param fm
	 */
	public void addField(EditableEntityFieldModel fm);

	
}