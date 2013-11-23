package org.middleheaven.domain.model;

import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.metaclass.MetaClass;
import org.middleheaven.model.annotations.InheritanceStrategy;
import org.middleheaven.util.QualifiedName;

/**
 * The entity metadata holder.
 */
public interface EntityModel  {

	
	public InheritanceStrategy getInheritanceStrategy();
	
	/**
	 * A {@link FieldModel} that represents the inheritance discriminator value.
	 * @return
	 */
	public FieldModel getDescriminatorFieldModel();
	
	/**
	 * Returns the discriminator value correspondent to the given type
	 * @param type
	 * @return
	 */
	public Object getDescriminatorValue(Class<?> type);
	
	/**
	 * 
	 * @return the name of the entity.
	 */
	public String getEntityName();
	
	/**
	 * 
	 * @return the entity class
	 */
	public MetaClass getEntityClass();
	
	/**
	 * 
	 * @param logicName the field's logic name
	 * @return the field entity model
	 */
	public EntityFieldModel fieldModel(QualifiedName logicName);
	
	/**
	 * @param fieldQualifiedName
	 * @return
	 */
	public boolean containsField(QualifiedName fieldQualifiedName);
	
	/**
	 * 
	 * @return the identity field entity model
	 */
	public EntityFieldModel identityFieldModel();
	
	/**
	 * 
	 * @return the identity type
	 */
	public MetaClass getIdentityType();
	
	/**
	 * 
	 * @return all entity's field entity model.
	 */
	public Enumerable<? extends EntityFieldModel> fields();

	/**
	 * Identifies if the entity identity is assigned by user code.
	 * @return <code>true</code> if the identity is assigned by user code, <code>false</code> if the system can assign an identity automaticly.
	 */
	public boolean isIdentityAssigned();

	/**
	 * @return
	 */
	public boolean isInheritanceRoot();
	
	/**
	 * @return
	 */
	public String getInheritanceRootEntityName();
}
