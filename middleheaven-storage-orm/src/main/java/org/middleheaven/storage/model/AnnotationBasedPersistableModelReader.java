package org.middleheaven.storage.model;

import java.util.Collection;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.DefaultReferenceDataTypeModel;
import org.middleheaven.domain.model.ModelingException;
import org.middleheaven.model.annotations.Id;
import org.middleheaven.model.annotations.ManyToMany;
import org.middleheaven.model.annotations.ManyToOne;
import org.middleheaven.model.annotations.NotNull;
import org.middleheaven.model.annotations.OneToMany;
import org.middleheaven.model.annotations.OneToOne;
import org.middleheaven.model.annotations.Version;
import org.middleheaven.persistance.db.metamodel.EditableColumnModel;
import org.middleheaven.persistance.db.metamodel.EditableDBTableModel;
import org.middleheaven.storage.annotations.Dataset;
import org.middleheaven.storage.annotations.Unique;
import org.middleheaven.util.collections.Enumerable;
import org.middleheaven.util.function.Maybe;


/**
 * Determines the persistance model from annotation found in the class.
 */
public class AnnotationBasedPersistableModelReader implements
		PersistableModelReader {

	
	@Override
	public void read(Class<? extends Object> type,	PersistableModelBuilder builder) {
		
		if (!isPersistableType(type)){
			return;
		}
		
		EditableDBTableModel tableModel = builder.getEditableModelOf(type);
		
		ClassIntrospector<? extends Object> inspector = Introspector.of(type);
		
		 Maybe<Dataset> maybeDataset = inspector.getAnnotation(Dataset.class);
		
		if (maybeDataset.isAbsent()) {
			tableModel.setName(type.getSimpleName());
		} else {
			tableModel.setName(maybeDataset.get().hardName());
		}
		
		Enumerable<PropertyAccessor> propertyAccessors = inspector.inspect().properties().retriveAll();

		if (propertyAccessors.isEmpty()){
			throw new ModelingException("No public properties found for entity " + type.getName() + ".");
		} else {

			for (PropertyAccessor pa : propertyAccessors){

				processField(pa, tableModel , builder);

			}

		}
	}

	private boolean isPersistableType(Class<? extends Object> type) {
		return !type.isEnum();
	}


	private void processField (PropertyAccessor pa ,EditableDBTableModel em, PersistableModelBuilder builder){

		
		EditableColumnModel fm = em.getColumnModel(pa.getName());
		
		//fm.setTransient(pa.isAnnotadedWith(Transient.class));
		fm.setVersion( pa.isAnnotadedWith(Version.class));
		fm.setUnique( pa.isAnnotadedWith(Unique.class));
		
		if (fm.isUnique()){
			Unique unique = pa.getAnnotation(Unique.class).get();
			fm.setUniqueGroup(unique.group());
			
			
		}
		

		fm.setNullable(!pa.isAnnotadedWith(NotNull.class));


	//	fm.setValueType(valueType);

		if(pa.isAnnotadedWith(Id.class)){
		//	Id key = pa.getAnnotation(Id.class).get();

			fm.setKey(true);
			fm.setUnique(true);
			fm.setUniqueGroup("#key");
			fm.setNullable(false);
			
		//	Class<?> identityType = mapAsIdentityField(fm,  key.type() , valueType, builder);
			
			
		} else if (pa.isAnnotadedWith(ManyToOne.class)){

			ManyToOne ref = pa.getAnnotation(ManyToOne.class).get();
		//	String fieldName = ref.targetIdentityField();
		//	mapAsManyToOne(fm,fieldName,valueType,builder);

		} else if (pa.isAnnotadedWith(OneToOne.class)){
		//	fm.setDataType(DataType.ONE_TO_ONE);
			OneToOne ref = pa.getAnnotation(OneToOne.class).get();
			String fieldName = ref.targetIdentityField();

		//	DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.ONE_TO_ONE);
		//	model.setTargetType(valueType);
		//	 EditableDomainEntityModel targetModel = builder.getEditableModelOf(valueType);

		//	if (fieldName.isEmpty()){
		//		fieldName = targetModel.identityFieldModel().getName().getName();
		//	} else {
		//		// TODO validate field exists
		//	}

		//	model.setTargetFieldType(this.resolveValidIdentityType(targetModel.getIdentityType()));

		//	fm.setDataTypeModel(model);


		} else if (pa.isAnnotadedWith(OneToMany.class)){
		//	fm.setDataType( DataType.ONE_TO_MANY);
			OneToMany ref = pa.getAnnotation(OneToMany.class).get();

			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.ONE_TO_MANY);

		//	model.setTargetType(ref.target());
		//	model.setAggregationType(valueType);

		//	fm.setDataTypeModel(model);



		} else if (pa.isAnnotadedWith(ManyToMany.class)){
		//	fm.setDataType(DataType.MANY_TO_MANY);
			ManyToMany ref = pa.getAnnotation(ManyToMany.class).get();

			DefaultReferenceDataTypeModel model = new DefaultReferenceDataTypeModel(DataType.MANY_TO_MANY);

		//	model.setTargetType(ref.target());
		//	model.setAggregationType(valueType);

		//	fm.setDataTypeModel(model);

		} else if( pa.getValueType().isEnum()) {
		//	fm.setDataType(DataType.ENUM);
		}

//		if ( fm.getDataType() == null){
//			if (matchTypes(valueType, String.class) ){
//				fm.setDataType(DataType.TEXT);
//
//				TextDataTypeModel model= new TextDataTypeModel();
//
//				fm.setDataTypeModel(model);
//
//				if(pa.isAnnotadedWith(EmailAddress.class)){
//					model.setEmailAddress(true);
//					model.setMaxLength(255);
//					model.setMinLength(0);
//
//				} else if(pa.isAnnotadedWith(Length.class)){
//					Length ref = pa.getAnnotation(Length.class);
//					model.setMaxLength(ref.max());
//					model.setMinLength(ref.min());
//				}
//
//				if(pa.isAnnotadedWith(NotEmpty.class)){
//					model.setEmptyable(false);
//				}
//
//
//
//				if(pa.isAnnotadedWith(Url.class)){
//					model.setUrl(true);
//				}
//
//			} else if (matchTypes(valueType,Integer.class ,int.class, Byte.class, byte.class, Short.class, short.class)  ){
//				fm.setDataType(DataType.INTEGER);
//			}  else if (matchTypes(valueType,CalendarDateTime.class)){
//				fm.setDataType(DataType.DATETIME);
//				if (pa.isAnnotadedWith(Temporal.class)){
//					fm.setDataType(pa.getAnnotation(Temporal.class).value());
//				} 
//			} else if (matchTypes(valueType,CalendarDate.class)){
//				fm.setDataType(DataType.DATE);
//			} else if (matchTypes(valueType, Date.class,Calendar.class)){
//				if (pa.isAnnotadedWith(Temporal.class)){
//					fm.setDataType(pa.getAnnotation(Temporal.class).value());
//				} else {
//					Log.onBookFor(this.getClass()).warn(
//							" {0} is to a too generic timestamp type. Consider annotate property {1} with @Temporal or use an instance of {2}", 
//							valueType.getName(),
//							fm.getSimpleName(), 
//							TimePoint.class.getName()
//					);
//					fm.setDataType(DataType.DATETIME);
//				}
//			} else if (matchTypes(valueType,Boolean.class,boolean.class)){
//				fm.setDataType(DataType.LOGIC);
//			} else if ( matchTypes(valueType,Double.class , double.class , Float.class , float.class, BigDecimal.class)){
//				fm.setDataType(DataType.DECIMAL);
//			} else if (matchTypes(valueType, Collection.class, Map.class)){
//				fm.setDataType( DataType.ONE_TO_MANY);
//			} else {
//
//				Log.onBookFor(this.getClass()).warn("Implicitly composing many-to-one relation for property " + pa.toString());
//
//				mapAsManyToOne(fm, "",valueType,builder);
//			}
//		}

	}

}
