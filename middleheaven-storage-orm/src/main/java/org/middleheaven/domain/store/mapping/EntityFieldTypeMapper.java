/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.storage.types.TypeMapper;
import org.middleheaven.util.QualifiedName;

/**
 * 
 */
public class EntityFieldTypeMapper implements TypeMapper {

	private EntityFieldModel field;
	private TypeMapper fieldType;
	private DataColumnModel[] columns;

	
	/**
	 * Constructor.
	 * @param field
	 * @param fieldType 
	 * @param columns 
	 */
	public EntityFieldTypeMapper(EntityFieldModel field, TypeMapper fieldType, DataColumnModel[] columns) {
		this.field = field;
		this.fieldType = fieldType;
		this.columns = CollectionUtils.duplicateArray(columns);
	}

	public DataColumnModel[] getColumns(){
		return CollectionUtils.duplicateArray(columns);
	}

	public QualifiedName getFieldName(){
		return field.getName();
	}
	
	public boolean isTransient(){
		return this.columns.length == 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return field.getValueType().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent, DataColumnModel... columns) {
		return this.fieldType.read(row, aggregateParent, this.columns);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object parent, Object object, DataRow row, DataColumnModel... columns) {
		 this.fieldType.write(parent, object, row, columns);
	}

}
