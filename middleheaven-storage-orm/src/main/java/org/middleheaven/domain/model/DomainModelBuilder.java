package org.middleheaven.domain.model;

import java.util.LinkedList;

import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.metaclass.ReflectionMetaClass;
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

		LinkedList<Class> stack = new LinkedList<Class>();
		
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
		

//	
//		new DependencyResolver(Log.onBookFor(this.getClass())).resolve(classes.getClasses(), new Starter<Class<?>>(){
//
//			@Override
//			public void inicialize(Class<?> type)
//			throws InicializationNotResolvedException,
//			InicializationNotPossibleException {
//
//				builder.buildModelFor(type, reader());
//				
//				
//			}
//
//			@Override
//			public void inicializeWithProxy(Class<?> dependableProperties)
//			throws InicializationNotResolvedException,
//			InicializationNotPossibleException {
//
//				throw new UnsupportedOperationException();
//			}
//
//			@Override
//			public List<Class<?>> sort(final Collection<Class<?>> dependencies) {
//
//				List<ClassDependency> deps = new ArrayList<ClassDependency>(dependencies.size());
//
//				for (Class<?> c: dependencies){
//					deps.add(new ClassDependency (c, dependencies));
//				}
//
//				Collections.sort(deps, new Comparator<ClassDependency>(){
//
//					@Override
//					public int compare(ClassDependency a,	ClassDependency b) {
//						return a.getDepenciesCount() - b.getDepenciesCount();
//					}
//
//				});
//				List<Class<?>> result = new ArrayList<Class<?>>(dependencies.size());
//				for (ClassDependency cd : deps){
//					result.add(cd.getType());
//				}
//
//				return result;
//			}
//
//			class ClassDependency {
//
//				private Class<?> type;
//				private Collection<Class<?>> depends;
//
//				public ClassDependency(Class<?> type, final Collection<Class<?>> dependencies){
//					this.type = type;
//
//					this.depends = Introspector.of(type).inspect().properties().retriveAll().collect(new Classifier<Class<?>,PropertyAccessor>(){
//
//						@Override
//						public Class<?> classify(PropertyAccessor obj) {
//							if( dependencies.contains(obj.getValueType())){
//								return obj.getValueType();
//							}
//							return null;
//						}
//
//					});
//
//
//				}
//
//				public int getDepenciesCount() {
//					return depends.size();
//				}
//
//				public Class getType(){
//					return type;
//				}
//
//			}
//
//			@Override
//			public boolean isRequired(Class<?> dependency) {
//				return true;
//			}
//
//		});
//
//		return builder.getModel();
	}

	/**
	 * @param c
	 * @return
	 */
	private boolean isDomainAbstraction(Class<?> c) {
		return !c.isInterface();
	}

	private class SimpleModelBuilder implements EntityModelBuildContext {

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
