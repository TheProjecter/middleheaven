/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.middleheaven.core.metaclass.MetaBean;
import org.middleheaven.core.metaclass.MetaClass;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.store.EntityInstance;
import org.middleheaven.domain.store.EntityInstanceField;
import org.middleheaven.domain.store.EntityManagerProxyHandler;
import org.middleheaven.domain.store.MetaBeanEntityInstance;
import org.middleheaven.domain.store.StorableState;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.reflection.inspection.ClassIntrospector;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.storage.types.TypeMapper;
import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class EntityInstanceTypeMapper implements TypeMapper {

	private Map<QualifiedName, EntityFieldTypeMapper> fieldTypes = new HashMap<QualifiedName, EntityFieldTypeMapper>();
	private EntityModel model;
	private Set<DataColumnModel> columns;

	/**
	 * Constructor.
	 * @param name
	 */
	public EntityInstanceTypeMapper(EntityModel model) {
		this.model = model;
	}


	public synchronized Collection<DataColumnModel> getColumns(){
		if (columns != null){
			return columns;
		}

		Set<DataColumnModel> cs = new HashSet<DataColumnModel>();
		for (EntityFieldTypeMapper f : fieldTypes.values()){
			cs.addAll(Arrays.asList(f.getColumns()));
		}

		this.columns = cs;
		return this.columns;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return model.getEntityClass().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent, DataColumnModel... columns) {

		final MetaClass entityClass = model.getEntityClass();

		MetaBean bean = entityClass.newInstance();

		EntityInstance instance = new MetaBeanEntityInstance(model, bean);


		for (EntityInstanceField f : instance.getFields()){
			if (!f.getModel().isTransient()){
				EntityFieldTypeMapper Function = this.fieldTypes.get(f.getModel().getName());

				Object value = Function.read(row,aggregateParent, columns);

				if (value instanceof EntityInstance){

					final ClassIntrospector<?> instrospector = Introspector.of(f.getModel().getValueType());

					value = instrospector.newProxyInstance(
							new EntityManagerProxyHandler((MetaBeanEntityInstance) value), 
							EntityInstance.class
							);

				}
				f.setValue(value);
			}
		}
		instance.setStorableState(StorableState.RETRIVED);
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object parent, Object object, DataRow row, DataColumnModel... columns) {


		if (object == null){

			for (int i =0; i < columns.length; i++){
				row.getColumn(columns[i].getName()).setValue(null);

			}

		} else {
			EntityInstance instance = (EntityInstance) object;

			if (parent == null){
				for (EntityInstanceField f : instance.getFields()){
					if (!f.getModel().isTransient()){
						EntityFieldTypeMapper Function = this.fieldTypes.get(f.getModel().getName());

						Function.write(object, f.getValue(), row, Function.getColumns());
					}
				}
			} else {

				EntityFieldModel fm = instance.getEntityModel().identityFieldModel();

				EntityFieldTypeMapper Function = this.fieldTypes.get(fm.getName());

				Function.write(object,instance.getIdentity(), row, columns);

			}

		}


	}

	/**
	 * @param fieldTypeMapper
	 */
	public void addFielTypeMapper(EntityFieldTypeMapper fieldTypeMapper) {
		fieldTypes.put(fieldTypeMapper.getFieldName(), fieldTypeMapper);
	}

	public EntityFieldTypeMapper getEntityFieldTypeMapper(QualifiedName fieldName){
		return fieldTypes.get(fieldName);
	}

}
