/**
 * 
 */
package org.middleheaven.domain.store.mapping;

import org.middleheaven.domain.model.EnumModel;
import org.middleheaven.persistance.DataRow;
import org.middleheaven.persistance.model.DataColumnModel;
import org.middleheaven.storage.types.TypeMapper;

/**
 * 
 */
public class EnumTypeMapper implements TypeMapper {

	private EnumModel enumModel;

	public EnumTypeMapper(EnumModel enumModel){
		this.enumModel = enumModel;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMappedClassName() {
		return enumModel.getEnumType().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object read(DataRow row, Object aggregateParent, DataColumnModel... columns) {
		
		Object value = row.getColumn(columns[0].getName()).getValue();
		
		if (value == null){
			return null;
		}
		return enumModel.getEnumFromValue(value);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(Object parent, Object object, DataRow row, DataColumnModel... columns) {
		
		if (object == null){
			row.getColumn(columns[0].getName()).setValue( null);
		} else {
			row.getColumn(columns[0].getName()).setValue( enumModel.getPersistableValue(object));
		}
		

	}

}
