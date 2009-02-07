package org.middleheaven.domain;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.storage.model.Column;
import org.middleheaven.storage.model.Table;


public class AnnotatedHardnameMapper extends HardnameMapper {

	@Override
	public String getFieldHardname(Class<?> type,	String logicFieldName) {
		String hardName =null;
		PropertyAccessor pa = ReflectionUtils.getPropertyAccessor(type,logicFieldName);
		if(pa.isAnnotatedWith(Column.class)){
			Column column = pa.getAnnotation(Column.class);
			String columnName = column.value();
			if (columnName.trim().isEmpty()){
				columnName = null;
			} else {
				hardName = columnName;
			}
		}
		
		if (hardName==null){
			hardName = this.mapFieldHardnameFromLogicname(logicFieldName);
		}
		
		
		return hardName;
	}

	@Override
	public String getEntityHardname(Class<?> type) {
		String hardEntityName=null;
		if (type.isAnnotationPresent(Table.class)){
			
			Table table = type.getAnnotation(Table.class);
			hardEntityName = table.value();
			if (hardEntityName.trim().isEmpty()){
				hardEntityName = null;
			}
		}
		
		if (hardEntityName==null){
			hardEntityName = this.mapEntityHardnameFromLogicname(type.getSimpleName());
		}

		return hardEntityName;
	}

}