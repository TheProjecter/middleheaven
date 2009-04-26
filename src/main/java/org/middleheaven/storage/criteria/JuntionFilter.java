package org.middleheaven.storage.criteria;

import org.middleheaven.core.reflection.PropertyAccessor;
import org.middleheaven.core.reflection.ReflectionUtils;
import org.middleheaven.storage.QualifiedName;
import org.middleheaven.storage.Storable;
import org.middleheaven.storage.StorableEntityModel;
import org.middleheaven.util.classification.BooleanClassifier;

public class JuntionFilter<T> implements BooleanClassifier<T> {

	QualifiedName fieldName;
	StorableEntityModel target;
	CriteriaFilter<T> subCriteria;
	
	public JuntionFilter(QualifiedName fieldName,StorableEntityModel target , Criteria<T> criteria) {
		super();
		subCriteria = new CriteriaFilter<T>(criteria, target);
		
		this.fieldName = fieldName;
		this.target = target;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean classify(T obj) {
		Object realValue = null;

		
		PropertyAccessor acessor =  ReflectionUtils.getPropertyAccessor(((Storable)obj).getPersistableClass(), fieldName.getName() );

		realValue = acessor.getValue(obj);

		return realValue!=null && subCriteria.classify((T)realValue);
	
	}

}