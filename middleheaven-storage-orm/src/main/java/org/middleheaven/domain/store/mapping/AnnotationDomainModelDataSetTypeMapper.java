/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.PropertyNotFoundException;
import org.middleheaven.core.reflection.inspection.ClassIntrospector;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.domain.model.DomainModel;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.model.EnumModel;
import org.middleheaven.domain.model.IdentityFieldDataTypeModel;
import org.middleheaven.model.annotations.Id;
import org.middleheaven.model.annotations.NotNull;
import org.middleheaven.model.annotations.Version;
import org.middleheaven.persistance.DataStoreSchemaName;
import org.middleheaven.persistance.db.mapping.IllegalModelStateException;
import org.middleheaven.persistance.model.ColumnValueType;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.persistance.model.DataColumnModelBean;
import org.middleheaven.persistance.model.EditableDataSet;
import org.middleheaven.storage.annotations.Column;
import org.middleheaven.storage.annotations.Columns;
import org.middleheaven.storage.annotations.Dataset;
import org.middleheaven.storage.annotations.Transient;
import org.middleheaven.storage.annotations.Type;
import org.middleheaven.storage.types.BooleanTypeMapper;
import org.middleheaven.storage.types.CalendarDateTimeTypeMapper;
import org.middleheaven.storage.types.CalendarDateTypeMapper;
import org.middleheaven.storage.types.CurrencyTypeMapper;
import org.middleheaven.storage.types.DateTypeMapper;
import org.middleheaven.storage.types.IdentityTypeMapper;
import org.middleheaven.storage.types.MoneyTypeMapper;
import org.middleheaven.storage.types.NumberTypeMapper;
import org.middleheaven.storage.types.RealTypeMapper;
import org.middleheaven.storage.types.StringTypeMapper;
import org.middleheaven.storage.types.TypeMapper;
import org.middleheaven.util.QualifiedName;
import org.middleheaven.util.function.Maybe;
import org.middleheaven.util.identity.IntegerIdentity;
import org.middleheaven.util.identity.LongIdentity;

/**
 * 
 */
public class AnnotationDomainModelDataSetTypeMapper implements DomainModelDataSetTypeMapper {

	private final Map<String, TypeMapper> types = new HashMap<String, TypeMapper>();

	private final Map<String, EntityModelDataSetMapping> mappings = new HashMap<String, EntityModelDataSetMapping>();
	private final DataStoreSchemaName dataStoreSchemaName;
	private final DomainModel domainModel;


	/**
	 * 
	 * @param domainModel
	 * @param dataMapper 
	 * @return
	 */
	public static AnnotationDomainModelDataSetTypeMapper newInstance(DataStoreSchemaName dataStoreSchemaName, DomainModel domainModel ){
		AnnotationDomainModelDataSetTypeMapper mapper = new AnnotationDomainModelDataSetTypeMapper(domainModel,dataStoreSchemaName);

		mapper.initialize();

		return mapper;
	}

	private AnnotationDomainModelDataSetTypeMapper(DomainModel domainModel, DataStoreSchemaName dataStoreSchemaName){
		this.dataStoreSchemaName = dataStoreSchemaName;
		this.domainModel = domainModel;

		this.registerTypeMapper(new NumberTypeMapper(Integer.class));
		this.registerTypeMapper(new NumberTypeMapper(Long.class));
		this.registerTypeMapper(new NumberTypeMapper(BigDecimal.class));
		this.registerTypeMapper(new NumberTypeMapper(BigInteger.class));
		this.registerTypeMapper(new NumberTypeMapper(Short.class));
		this.registerTypeMapper(new NumberTypeMapper(Byte.class));
		this.registerTypeMapper(new BooleanTypeMapper());
		this.registerTypeMapper(new StringTypeMapper());
		this.registerTypeMapper(new DateTypeMapper());
		this.registerTypeMapper(new CalendarDateTypeMapper());
		this.registerTypeMapper(new CalendarDateTimeTypeMapper());
		this.registerTypeMapper(new MoneyTypeMapper());
		this.registerTypeMapper(new CurrencyTypeMapper());
		this.registerTypeMapper(new RealTypeMapper());
		this.registerTypeMapper(new IdentityTypeMapper(IntegerIdentity.class));
		this.registerTypeMapper(new IdentityTypeMapper(LongIdentity.class));


	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityModelDataSetMapping getMappingFor(EntityModel entityModel) {

		return mappings.get(entityModel.getEntityName().toLowerCase());

	}

	/**
	 * {@inheritDoc}
	 */
	private void initialize() {

		// because of dependencies, a queue must be used
		Map<String, EntityModel> queue = new HashMap<String, EntityModel>();

		for (EntityModel model : domainModel.models()){

			queue.put(model.getEntityClass().getSimpleName(), model);

		}

		while(!queue.isEmpty()){

			processType(queue , queue.keySet().iterator().next());
		}
	}

	/**
	 * @param model
	 */
	private TypeMapper processType(Map<String, EntityModel> queue ,String typeName) {


		EntityModel model = queue.remove(typeName);


		EditableEntityModelDataSetMapping mapping = new EditableEntityModelDataSetMapping();

		mappings.put(model.getEntityName().toLowerCase(), mapping);

		mapping.setDataStoreSchemaName(this.dataStoreSchemaName);

		readDataSetName(mapping , model);

		TypeMapper type = types.get(model.getEntityClass().getName());

		if (type == null){
			type = createEntityType(mapping , model, queue);
		}

		mapping.setTypeMapping((EntityInstanceTypeMapper) type);

		return type;
	}

	/**
	 * @param model
	 * @param queue 
	 * @return
	 */
	private EntityInstanceTypeMapper createEntityType(EditableEntityModelDataSetMapping mapping , EntityModel model, Map<String, EntityModel> queue) {

		EntityInstanceTypeMapper type = new EntityInstanceTypeMapper(model);

		mapping.addInstanceMapper(model.getEntityName(), type);

		EditableDataSet dsModel = new EditableDataSet(); 

		dsModel.setName(mapping.getDataSetName());

		for (EntityFieldModel field : model.fields()){

			if (!field.isTransient() && !field.getDataType().isToManyReference()){
				try {
					PropertyAccessor pa = model.getEntityClass().getPropertyAcessor(field.getName().getDesignation());

					TypeMapper fieldType = readFieldTypeMapper(pa, field, queue);

					DataColumnModel[] columns = readColumns(dsModel, pa, field , mapping);



					EntityFieldTypeMapper fieldTypeMapper = new EntityFieldTypeMapper(field, fieldType, columns);



					type.addFielTypeMapper(fieldTypeMapper);
				} catch (PropertyNotFoundException e){
					throw new DataSetColumnNotMatchPropertyMappingException(e.getPropertyName(), e.getTypeName());
				}
			}


		}

		return type;

	}



	/**
	 * @param dsModel 
	 * @param pa
	 * @param field
	 * @param mapping 
	 */
	private DataColumnModel[] readColumns(EditableDataSet dsModel, PropertyAccessor pa, EntityFieldModel field, EditableEntityModelDataSetMapping mapping) {

		Column[] all;

		Maybe<Columns> maybeColumns = pa.getAnnotation(Columns.class);

		if (maybeColumns.isAbsent()){

			Maybe<Column> maybeColumn = pa.getAnnotation(Column.class);

			if (maybeColumn.isAbsent()){

				if (pa.isAnnotadedWith(Transient.class)) {
					all = new Column[0];
				} else {

					// auto mappping

					return columnBeanFromField(dsModel, field); 

				}

			} else {
				all = new Column[]{maybeColumn.get()};
			}

		} else {
			all = maybeColumns.get().columns();
		}

		DataColumnModel[] columnModels = new DataColumnModel[all.length];

		for (int i = 0 ; i < all.length; i++){

			DataColumnModelBean cm = new DataColumnModelBean();

			Column c = all[i];

			cm.setName(QualifiedName.qualify(dsModel.getName(), c.hardName()));
			cm.setDataSetModel(dsModel);
			cm.setNullable(!pa.isAnnotadedWith(NotNull.class));
			cm.setPrecision(c.precision());
			cm.setSize(c.length());

			cm.setType(this.mapColumnTypeFromEntityField(field));

			cm.setVersion(!pa.isAnnotadedWith(Version.class));

			if (pa.isAnnotadedWith(Id.class)){
				cm.setPrimaryKeyGroup("key");
			}

			dsModel.addColumn(cm);
			columnModels[i] = cm;


		}

		return columnModels;
	}




	/**
	 * @param dsModel 
	 * @param field
	 * @return
	 */
	private DataColumnModel[] columnBeanFromField(EditableDataSet dsModel, EntityFieldModel field) {
		DataColumnModel[] bean = new DataColumnModel[1];

		DataColumnModelBean column = new DataColumnModelBean();

		column.setName(QualifiedName.qualify(dsModel.getName(), field.getName().getDesignation()));
		column.setNullable(field.isNullable());
		column.setType(mapColumnTypeFromEntityField(field));
		column.setVersion(field.isVersion());
		column.setDataSetModel(dsModel);

		if (field.isIdentity()){
			column.setPrimaryKeyGroup("key");
		}

		dsModel.addColumn(column);
		bean[0] = column;
		return bean;
	}


	/**
	 * @param dataType
	 * @return
	 */
	private ColumnValueType mapColumnTypeFromEntityField(EntityFieldModel field) {
		switch (field.getDataType()){
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
		case TEXT:
			return ColumnValueType.TEXT;
		case MEMO:
			return ColumnValueType.CLOB;
		case TIME:
			return ColumnValueType.TIME;
		case MANY_TO_ONE:
		case ONE_TO_ONE:
			// TODO composed reference key
			return ColumnValueType.INTEGER;
		case ONE_TO_MANY:
		case MANY_TO_MANY:
		case UNKONW:
		default:
			throw new IllegalArgumentException("Cannot map datatype " + field.getDataType() + "to a column type" );
		}
	}

	/**
	 * @param field
	 * @param queue 
	 * @return
	 */
	private TypeMapper readFieldTypeMapper(PropertyAccessor pa, EntityFieldModel field, Map<String, EntityModel> queue) {


		Maybe<Type> tm = pa.getAnnotation(Type.class);

		if (!tm.isAbsent()){
			return Introspector.of(tm.get().type()).newInstance();
		}

		if (domainModel.containsModelFor(pa.getValueType().getName())){
			// it is an entity. return it typemapper

			EntityModelDataSetMapping map = this.mappings.get(pa.getValueType().getSimpleName().toLowerCase());

			if (map == null){
				// recursive call
				return processType(queue, pa.getValueType().getSimpleName());
			} 

			return map.getTypeMapper();

		} else if (pa.getValueType().isEnum()) {
		
				Maybe<EnumModel> model = domainModel.getEmumModel((Class<? extends Enum>)pa.getValueType());
				
				if (model.isAbsent()){
					throw new IllegalModelStateException("No model defined for enum " + pa.getValueType());
				}
				
				return new EnumTypeMapper(model.get());

		} else {

			String typeName;

			final ClassIntrospector<?> introspector = Introspector.of(pa.getValueType());
			if (introspector.isPrimitive()){
				typeName = introspector.getPrimitiveWrapper().getName();
			} else {
				typeName = pa.getValueType().getName();
			}

			// search default
			TypeMapper m = types.get(typeName);

			if (m == null){

				if (field.isIdentity()) {

					final Maybe<Id> maybetId = pa.getAnnotation(Id.class);

					if (maybetId.isAbsent()){
						return new IdentityTypeMapper( ((IdentityFieldDataTypeModel)field.getDataTypeModel()).getIdentityType() );
					} else {
						Id id = maybetId.get();

						return new IdentityTypeMapper(id.type());
					}

				}
				throw new IllegalStateException("No TypeMapping found for class " + pa.getValueType().getName()); 
			}

			return m;
		}


	}



	/**
	 * @param mapping
	 * @param model 
	 * @param class1
	 */
	private void readDataSetName(EditableEntityModelDataSetMapping mapping, EntityModel model) {

		Dataset ds = model.getEntityClass().getAnnotation(Dataset.class);

		if (ds == null || ds.hardName().length() == 0){
			mapping.setDataSetName(resolveDataSetNameFromEntity(model));
		} else {
			mapping.setDataSetName(ds.hardName());
			
		}

		//mapping.setInherintance(model.getInheritanceStrategy());

	}



	protected String resolveDataSetNameFromEntity(EntityModel model){
		return model.getEntityName().toLowerCase();
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerTypeMapper(TypeMapper typeMapper) {
		this.types.put(typeMapper.getMappedClassName(), typeMapper);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregisterTypeMapper(TypeMapper typeMapper) {
		this.types.remove(typeMapper.getMappedClassName());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EntityFieldTypeMapper getEntityFieldTypeMapper(QualifiedName fieldName) {
		final EntityModelDataSetMapping entityModelDataSetMapping = this.mappings.get(fieldName.getQualifier().toLowerCase());
		if (entityModelDataSetMapping == null){
			throw new IllegalStateException("No mapping for entity " + fieldName.getQualifier());
		}
		return entityModelDataSetMapping.getInstanceTypeMapper(fieldName.getQualifier()).getEntityFieldTypeMapper(fieldName);
	}






}
