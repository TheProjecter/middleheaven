package org.middleheaven.domain.model;

import org.middleheaven.collections.Enumerable;
import org.middleheaven.core.reflection.metaclass.MetaClass;
import org.middleheaven.model.annotations.InheritanceStrategy;
import org.middleheaven.util.QualifiedName;

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

	public void setInheritanceStrategy(InheritanceStrategy strategy);
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

	public void addDescriminatorValue( Class<?> type , Object value);

	/**
	 * @param em
	 */
	public void copyFieldTo(EditableDomainEntityModel em);

	/**
	 * @param name
	 * @return
	 */
	public boolean hasField(String name);
	
	public void setInheritanceRoot(String entityName);
	
}
