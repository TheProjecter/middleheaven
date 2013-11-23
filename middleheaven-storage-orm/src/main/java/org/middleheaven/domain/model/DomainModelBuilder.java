package org.middleheaven.domain.model;

import java.util.Deque;
import java.util.LinkedList;

import org.middleheaven.core.metaclass.ReflectionMetaClass;
import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.model.AbstractModelBuilder;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;

/**
 * 
 */
public final class DomainModelBuilder extends AbstractModelBuilder<EntityModel, DomainModel, DomainModelReader > {


	public DomainModelBuilder(){
		addReader(new AnnotatedModelReader());
	}

	public DomainModel build(ClassSet classes){
		
		final SimpleModelBuilder builder = new SimpleModelBuilder();

		Deque<Class> stack = new LinkedList<Class>();
		
		for (Class c : classes){
			if (isDomainAbstraction(c)){
				// inheritance needs to process the parent first
				if (ClassIntrospector.of(c).getRootParent().isAbsent()){
					stack.addFirst(c);
				} else {
					stack.addLast(c);
				}
			}
		}
		
		for (Class c : stack){
			reader().read(c, builder);
		}
		
		DomainModel model =  builder.getModel();
		
		
		for (EntityModel em : model.models()){
			
			for (EntityFieldModel fm : em.fields()){
				if (!fm.isTransient() && fm.getDataType().isReference()){
					DefaultReferenceDataTypeModel typeModel = (DefaultReferenceDataTypeModel) fm.getDataTypeModel();
					
					EditableDomainEntityModel targetModel =  (EditableDomainEntityModel) model.getModelFor(typeModel.getTargetType().getName());

					if (targetModel == null){
						// TODO Embeded and ValueObject aka userType
						throw new IllegalModelStateException("Reference type " + fm.getValueType().getName() + " for field " + fm.getName() +  " is not an entity");
					}
					String fieldName = targetModel.identityFieldModel().getName().getDesignation();
					
					typeModel.setTargetFieldName(fieldName);
					typeModel.setTargetFieldType(targetModel.getIdentityType());
					typeModel.setTargetFieldDataType(targetModel.identityFieldModel().getDataType());
				}
			}
			
		}
		
		return model;
	}
	
	/**
	 * @param c
	 * @return
	 */
	private boolean isDomainAbstraction(Class<?> c) {
		return !c.isInterface();
	}

	private static class SimpleModelBuilder implements EntityModelBuildContext {

		EditableDomainModel model = new EditableDomainModel();
		
		@Override
		public EditableDomainEntityModel getEditableModelOf(Class<?> type) {
			EntityModel em = model.getModelFor(type.getName());
			if (em == null){
				em = new HashEditableEntityModel(new ReflectionMetaClass(type));
				model.addModel(type.getName(), em);
			}
			return (EditableDomainEntityModel) em;
		}


		public DomainModel getModel() {
			return model;
		}


		@Override
		public EditableEnumModel getEnumModel(Class enumType, Class persistableType) {
			SimpleEnumModel enumModel = new SimpleEnumModel(enumType, persistableType);
			model.addEnumModel(enumModel);
			return enumModel;
		}
	
	}
}
