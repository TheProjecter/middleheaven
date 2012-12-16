/**
 * 
 */
package org.middleheaven.storage.dataset.mapping;

import org.middleheaven.core.reflection.ClassSet;
import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.DataTypeModel;
import org.middleheaven.domain.model.DefaultReferenceDataTypeModel;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.model.FieldModel;
import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.storage.annotations.Column;
import org.middleheaven.storage.annotations.Dataset;
import org.middleheaven.storage.annotations.Indexed;
import org.middleheaven.storage.annotations.Transient;
import org.middleheaven.storage.annotations.Unique;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.function.Block;
import org.middleheaven.util.function.Maybe;

/**
 * 
 */
public class AnnotationsDomaniModelDrivenDatasetModelReader implements
DatasetRepositoryModelReader {


	public static AnnotationsDomaniModelDrivenDatasetModelReader newInstance(ClassSet domainClasses, DomainModel domainModel){
		return new AnnotationsDomaniModelDrivenDatasetModelReader( domainClasses,  domainModel);
	}

	private final ClassSet domainClasses;
	private final DomainModel domainModel;

	private AnnotationsDomaniModelDrivenDatasetModelReader(ClassSet domainClasses, DomainModel domainModel){
		this.domainClasses = domainClasses;
		this.domainModel = domainModel;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void read(EditableDatasetRepositoryModel model) {
		for (Class c : domainClasses){
			if(!c.isInterface() && !c.isEnum()){
				map(c, model);
			}
		}
	}

	/**
	 * @param c
	 * @param model
	 */
	private <T> void map(Class<T> c, EditableDatasetRepositoryModel model) {

		final EntityModel entityModel = domainModel.getModelFor(c.getName());

		if (entityModel == null) {
			return;
		}
		
	
		String rootEntityName = entityModel.getEntityName();
		
		if (entityModel.getInheritanceStrategy().hasInheritance() && !entityModel.isInheritanceRoot()){
			rootEntityName = entityModel.getInheritanceRootEntityName();
		}
		
		final String entityName = rootEntityName;
				
		EditableDatasetModel existingModel = model.getDataSetModel(entityName);

		final EditableDatasetModel dsModel;

		if (existingModel != null){
			dsModel = existingModel;
		} else {
			dsModel = new HashDatasetModel();
			dsModel.setName(entityName);
			model.addDatasetModel(dsModel);
		}

		final ClassIntrospector<T> introspector = ClassIntrospector.of(c);
		Maybe<Dataset> maybeDataSet = introspector.getAnnotation(Dataset.class);

		String hardDatasetName = dsModel.getName(); // TODO add NameTransformation 

		if (!maybeDataSet.isAbsent()){
			final Dataset dataset = maybeDataSet.get();

			hardDatasetName = StringUtils.maybe(dataset.hardName()).or(dsModel.getName());

		} 


		dsModel.setHardName(hardDatasetName);

		introspector.inspect().properties().each(new Block<PropertyAccessor>(){

			@Override
			public void apply(PropertyAccessor object) {
				EditableDatasetColumnModel columnModel = dsModel.getColumn(object.getName()); 

				FieldModel fieldModel = entityModel.fieldModel(QualifiedName.qualify(entityName, object.getName()));

				if (!fieldModel.isTransient() && !fieldModel.getDataType().isToManyReference() && !object.isAnnotadedWith(Transient.class)) {

					if (columnModel == null){
						columnModel = new HashDatasetColumnModel();
						columnModel.setName(object.getName());
						dsModel.addDatasetColumnModel(columnModel); 
					}

					Maybe<Column> maybeColumn = object.getAnnotation(Column.class);

					if (maybeColumn.isPresent()){

						Column column = maybeColumn.get();

						columnModel.setHardName(StringUtils.maybe(column.hardName()).or(columnModel.getName()));
						columnModel.setNullable(column.nullable());
						columnModel.setScale(column.scale());
						columnModel.setSize(column.length());

					}

					columnModel.setVersion(fieldModel.isVersion()); 

					columnModel.setKey(fieldModel.isIdentity());

					columnModel.setIndexed(object.isAnnotadedWith(Indexed.class) || fieldModel.isDiscriminator() || columnModel.isKey());


					columnModel.setUnique(fieldModel.isUnique());
					columnModel.setUniqueGroupName(fieldModel.getUniqueGroup());

					Maybe<Unique> maybeUnique = object.getAnnotation(Unique.class);

					if (maybeUnique.isPresent()){
						// override
						columnModel.setUnique(true);
						columnModel.setUniqueGroupName(StringUtils.maybe(maybeUnique.get().group()).or(columnModel.getName()));
					} 

					columnModel.setValueType(valueTypeOf(fieldModel));

					dsModel.addDatasetColumnModel(columnModel);
				}

			}

		});



	}


	/**
	 * @param fieldModel
	 * @return
	 */
	protected ColumnValueType valueTypeOf(FieldModel fieldModel) {
		try {
			switch (fieldModel.getDataType()){
			case MANY_TO_ONE:
			case ONE_TO_ONE:
				DefaultReferenceDataTypeModel typeModel = (DefaultReferenceDataTypeModel)fieldModel.getDataTypeModel();
				return valueTypeOf(typeModel.getTargetFieldDataType());
			default:
				return valueTypeOf(fieldModel.getDataType());
			}
		}catch (IllegalStateException e){
			throw new IllegalStateException("Cannot map field " + fieldModel.getName() + " to a valid " + ColumnValueType.class, e);
		}

	}

	protected ColumnValueType valueTypeOf(DataType dataType) {
		switch (dataType){
		case DATE:
			return ColumnValueType.DATE;
		case DATETIME:
			return ColumnValueType.DATETIME;
		case DECIMAL:
			return ColumnValueType.DECIMAL;
		case STATUS:
		case ENUM:
			return ColumnValueType.SMALL_INTEGER;
		case INTEGER:
			return ColumnValueType.INTEGER;
		case LOGIC:
			return ColumnValueType.LOGIC;
		case MEMO:
			return ColumnValueType.MEMO;
		case TEXT:
			return ColumnValueType.TEXT;
		case TIME:
			return ColumnValueType.TIME;
		case MANY_TO_ONE:
		case ONE_TO_ONE:
		case MANY_TO_MANY:
		case ONE_TO_MANY:
			// transient
		case UNKONW:
		default :
			throw new IllegalStateException("Cannot map " + dataType + " to a valid " + ColumnValueType.class);
		}
	}

}