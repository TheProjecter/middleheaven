package org.middleheaven.domain.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionException;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.EnumIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.core.reflection.metaclass.MetaClass;
import org.middleheaven.core.reflection.metaclass.ReflectionMetaClass;
import org.middleheaven.logging.Logger;
import org.middleheaven.model.annotations.Discriminator;
import org.middleheaven.model.annotations.DiscriminatorValue;
import org.middleheaven.model.annotations.EmailAddress;
import org.middleheaven.model.annotations.Entity;
import org.middleheaven.model.annotations.Id;
import org.middleheaven.model.annotations.Inheritance;
import org.middleheaven.model.annotations.InheritanceStrategy;
import org.middleheaven.model.annotations.Length;
import org.middleheaven.model.annotations.ManyToMany;
import org.middleheaven.model.annotations.ManyToOne;
import org.middleheaven.model.annotations.NotEmpty;
import org.middleheaven.model.annotations.NotNull;
import org.middleheaven.model.annotations.OneToMany;
import org.middleheaven.model.annotations.OneToOne;
import org.middleheaven.model.annotations.Temporal;
import org.middleheaven.model.annotations.Url;
import org.middleheaven.model.annotations.Version;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;
import org.middleheaven.quantity.time.CalendarDate;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.quantity.time.TimePoint;
import org.middleheaven.storage.annotations.Transient;
import org.middleheaven.storage.annotations.Unique;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.function.Maybe;
import org.middleheaven.util.identity.Identity;

/**
 * Reader that relies on annotations within the class.
 * 
 */
public class AnnotatedModelReader implements DomainModelReader {

	
	Logger logger = Logger.onBookFor(this.getClass());
	
	/**
	 * 
	 * Constructor.
	 */
	public AnnotatedModelReader (){}
	
	
	private boolean matchTypes(Class<?> candidate , Class<?> ... types){
		for (Class<?> t : types){
			if (candidate.isAssignableFrom(t)){
				return true;
			}
		}
		return false;
	}

	
	@Override
	public void read(Class<?> type, EntityModelBuildContext context) {
		
		if (type.isEnum()) {
			
			readEnum((Class<? extends Enum>) type, context);
			return;
		}
		
		if (!isEntityType(type)){
			return;
		}
		
		EditableDomainEntityModel em = context.getEditableModelOf(type);
		
		
		final ClassIntrospector<?> classInstrospector = ClassIntrospector.of(type);
		Maybe<Inheritance> maybeInherintance = classInstrospector.getAnnotation(Inheritance.class); // TODO can be found in super ?

		EditableDomainEntityModel rootModel = em;
		if (maybeInherintance.isAbsent()){
			
			// look in super
			Maybe<Class> maybeRoot = classInstrospector.getRootParent();
			
			if (maybeRoot.isAbsent()){
				em.setInheritanceStrategy(InheritanceStrategy.NO_INHERITANCE);
			} else {
				rootModel = context.getEditableModelOf(maybeRoot.get());
				
				rootModel.copyFieldTo(em);
				em.setInheritanceStrategy(rootModel.getInheritanceStrategy());
				em.setInheritanceRoot(rootModel.getEntityName());
			}
		} else {
			em.setInheritanceStrategy(maybeInherintance.get().strategy());
			em.setInheritanceRoot(rootModel.getEntityName());
		}
		
		Maybe<DiscriminatorValue> maybeDiscriminatorValue = classInstrospector.getAnnotation(DiscriminatorValue.class);
		
		if ( maybeDiscriminatorValue.isAbsent()){
			if (rootModel.getInheritanceStrategy().isSingleDataset()){
				throw new IllegalModelStateException(
						"Single dataset inherintance strategy must define a discriminator value for " + em.getEntityName()
				);
			}
		} else {
			rootModel.addDescriminatorValue(type, maybeDiscriminatorValue.get().value());
		}
		
		Enumerable<PropertyAccessor> propertyAccessors = Introspector.of(type).inspect().properties().retriveAll();

		if (propertyAccessors.isEmpty()){
			throw new ModelingException("No public properties found for entity " + type.getName() + ".");
		} else {

			for (PropertyAccessor pa : propertyAccessors){

				if (!em.hasField(pa.getName())){
					processField(pa,em, context);
				}

			}

			if (em.identityFieldModel() == null){
				throw new ModelingException("No identity properties found for entity " + type.getName() + ".");
			}
		}

	}


	private void readEnum(Class<? extends Enum> enumType, EntityModelBuildContext context) {
		
		
		// find @DescriminatorValue
		
		Method m = Introspector.of(enumType).inspect().methods().annotatedWith(Discriminator.class).retrive();
		
		if (m == null){
			throw new IllegalModelStateException("Enum type " + enumType.getName() + " must have a method anotated with @Discriminator");
		}
		
		EditableEnumModel model = context.getEnumModel(enumType, m.getReturnType());
		
		for (Object value : EnumIntrospector.of(enumType).getValues()){
			
			try {
				model.addValueMaping(value, m.invoke(value));
			} catch (IllegalArgumentException e) {
				throw ReflectionException.manage(e, enumType);
			} catch (IllegalAccessException e) {
				throw ReflectionException.manage(e, enumType);
			} catch (InvocationTargetException e) {
				throw ReflectionException.manage(e, enumType);
			}
		}
		
	}


	private boolean isEntityType(Class<?> type) {
		if (!type.isAnnotationPresent(Entity.class) && !type.isEnum()){
			logger.debug("Class {0} was ignored as an entity because is not annotated with @Entity", type.getName());
		}
		return !type.isEnum() && type.isAnnotationPresent(Entity.class);
	}

	private MetaClass resolveValidIdentityType(Class<?> type){
		if (type.isInterface() || Modifier.isAbstract(type.getModifiers())){
			throw new ModelingException("Cannot use interfaces or abstract types as identity type. Please use a concrete type.");
		}

		return new ReflectionMetaClass(type);
	}

	private void processField (PropertyAccessor pa ,EditableDomainEntityModel em, EntityModelBuildContext builder){

	
		final QualifiedName fieldQualifiedName = QualifiedName.qualify(em.getEntityName(), pa.getName());
	
		EditableEntityFieldModel fm;
		
		if (em.containsField(fieldQualifiedName)) {
			fm = em.fieldModel(fieldQualifiedName);
			
		} else {
			fm = new BeanEditableEntityFieldModel(em.getEntityName(), pa.getName());
			
			em.addField(fm);
		}
		
		fm.setTransient(pa.isAnnotadedWith(Transient.class) || !pa.isWritable());
		fm.setVersion( pa.isAnnotadedWith(Version.class));
		fm.setUnique( pa.isAnnotadedWith(Unique.class));
		fm.setNullable(!pa.isAnnotadedWith(NotNull.class));
		fm.setDiscriminator(!pa.isAnnotadedWith(Discriminator.class));
		
		Class<?> valueType = pa.getValueType();

		fm.setValueType(valueType);

		if(pa.isAnnotadedWith(Id.class)){
			Id key = pa.getAnnotation(Id.class).get();

			fm.setIdentity(true);
			fm.setUnique(true);
			fm.setNullable(false);

			Class<?> identityType = mapAsIdentityField(fm,  key.type() , valueType, builder);
			
			
			em.setIdentityType(resolveValidIdentityType(identityType));
			
			IdentityFieldDataTypeModel model = new IdentityFieldDataTypeModel(DataType.INTEGER); // derive from valueType or key type
			
			model.setIdentityType(key.type());
			
			fm.setDataTypeModel(model);
			
		} else if (pa.isAnnotadedWith(ManyToOne.class)){

			ManyToOne ref = pa.getAnnotation(ManyToOne.class).get();
			String fieldName = ref.targetIdentityField();

			mapAsManyToOne(fm,fieldName,valueType,builder);

		} else if (pa.isAnnotadedWith(OneToOne.class)){
			fm.setDataType(DataType.ONE_TO_ONE);
			OneToOne ref = pa.getAnnotation(OneToOne.class).get();
			String fieldName = ref.targetIdentityField();

			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.ONE_TO_ONE);

			model.setTargetType(new ReflectionMetaClass(valueType));

			 EditableDomainEntityModel targetModel = builder.getEditableModelOf(valueType);

			if (fieldName.isEmpty()){
				fieldName = targetModel.identityFieldModel().getName().getDesignation();
			} else {
				// TODO validate field exists
			}

			model.setTargetFieldType(targetModel.getIdentityType());

			fm.setDataTypeModel(model);


		} else if (pa.isAnnotadedWith(OneToMany.class)){
			fm.setDataType( DataType.ONE_TO_MANY);
			OneToMany ref = pa.getAnnotation(OneToMany.class).get();

			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.ONE_TO_MANY);

			model.setTargetType(new ReflectionMetaClass(ref.target()));
			model.setAggregationType(new ReflectionMetaClass(valueType));

			fm.setDataTypeModel(model);



		} else if (pa.isAnnotadedWith(ManyToMany.class)){
			fm.setDataType(DataType.MANY_TO_MANY);
			ManyToMany ref = pa.getAnnotation(ManyToMany.class).get();

			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.MANY_TO_MANY);

			model.setTargetType(new ReflectionMetaClass(ref.target()));
			model.setAggregationType(new ReflectionMetaClass(valueType));

			fm.setDataTypeModel(model);

		} else if( pa.getValueType().isEnum()) {
			fm.setDataType(DataType.ENUM);
		} else if (Collection.class.isAssignableFrom(pa.getValueType())){
			fm.setDataType( DataType.ONE_TO_MANY);
		}

		if ( fm.getDataType() == null){
			if (matchTypes(valueType, String.class) ){
				fm.setDataType(DataType.TEXT);

				TextDataTypeModel model= new TextDataTypeModel();

				fm.setDataTypeModel(model);

				if(pa.isAnnotadedWith(EmailAddress.class)){
					model.setEmailAddress(true);
					model.setMaxLength(255);
					model.setMinLength(0);

				} else if(pa.isAnnotadedWith(Length.class)){
					Length ref = pa.getAnnotation(Length.class).get();
					model.setMaxLength(ref.max());
					model.setMinLength(ref.min());
				}

				if(pa.isAnnotadedWith(NotEmpty.class)){
					model.setEmptyable(false);
				}



				if(pa.isAnnotadedWith(Url.class)){
					model.setUrl(true);
				}

			} else if (matchTypes(valueType,Integer.class ,int.class, Byte.class, byte.class, Short.class, short.class, Long.class, long.class)  ){

				fm.setDataType(DataType.INTEGER);
				
			} else if ( matchTypes(valueType,Double.class , double.class , Float.class , float.class, BigDecimal.class)){
				fm.setDataType(DataType.DECIMAL);
			} else if (matchTypes(valueType,CalendarDateTime.class)){
				fm.setDataType(DataType.DATETIME);
				if (pa.isAnnotadedWith(Temporal.class)){
					fm.setDataType(pa.getAnnotation(Temporal.class).get().value());
				} 
			} else if (matchTypes(valueType,CalendarDate.class)){
				fm.setDataType(DataType.DATE);
			} else if (matchTypes(valueType, Date.class,Calendar.class)){
				if (pa.isAnnotadedWith(Temporal.class)){
					fm.setDataType(pa.getAnnotation(Temporal.class).get().value());
				} else {
					logger.warn(
							" {0} is to a too generic timestamp type. Consider annotate property {1} with @Temporal or use an instance of {2}", 
							valueType.getName(),
							fm.getName(), 
							TimePoint.class.getName()
					);
					fm.setDataType(DataType.DATETIME);
				}
			} else if (matchTypes(valueType,Boolean.class,boolean.class)){
				fm.setDataType(DataType.LOGIC);
			} else if (matchTypes(valueType, Collection.class, Map.class)){
				fm.setDataType( DataType.ONE_TO_MANY);
			} else {

				logger.warn("Implicitly composing many-to-one relation for property " + pa.toString());

				mapAsManyToOne(fm, "",valueType,builder);
			}
		}

	}
	

	private void mapAsManyToOne(EditableEntityFieldModel fm,String fieldName,Class<?> valueType , final EntityModelBuildContext builder ){

		
		if (valueType.isEnum()){
			fm.setDataType(DataType.ENUM);
			
			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.ENUM);

			model.setTargetType(new ReflectionMetaClass(valueType));
			
			fm.setDataTypeModel(model);
		} else if (Identity.class.isAssignableFrom(valueType)){
			mapAsIdentityField(fm, null , valueType, builder);
		} else {
			fm.setDataType(DataType.MANY_TO_ONE);


			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.MANY_TO_ONE);

			model.setTargetType(new ReflectionMetaClass(valueType));
			
//			EditableDomainEntityModel targetModel = builder.getEditableModelOf(valueType);
//
//			if (fieldName==null || fieldName.isEmpty()){
//				fieldName = targetModel.identityFieldModel().getName().getName();
//			}else {
//				// TODO validate field exists
//			}
//			model.setTargetFieldName(fieldName);
//			model.setTargetFieldType(targetModel.getIdentityType());

			fm.setDataTypeModel(model);
		}
	}

	
	private Class<?> mapAsIdentityField (EditableEntityFieldModel fm,Class<?> idType ,Class<?> valueType, EntityModelBuildContext builder ){
		Class<?> identityType = valueType;
		if(identityType.isInterface() || Modifier.isAbstract(identityType.getModifiers())){
			identityType = idType;
			if(identityType==null || Void.class.equals(identityType)){
				throw new ModelingException("Illegal identity type for " 
						+ fm.getName().getQualifier()
						+ ".When using interfaces or abstract types as identity type you must specify a non interface, non abstract, class for identity");
			}
		}
		fm.setDataType(DataType.fromClass(identityType));

		
		return identityType;
	}


}
