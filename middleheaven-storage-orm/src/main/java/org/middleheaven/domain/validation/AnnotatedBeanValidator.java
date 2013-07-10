package org.middleheaven.domain.validation;

import org.middleheaven.domain.model.DataType;
import org.middleheaven.domain.model.EntityFieldModel;
import org.middleheaven.domain.model.EntityModel;
import org.middleheaven.domain.model.TextDataTypeModel;
import org.middleheaven.validation.BeanValidator;
import org.middleheaven.validation.CompositeValidator;
import org.middleheaven.validation.LengthValidator;
import org.middleheaven.validation.NotEmptyValidator;
import org.middleheaven.validation.NotNullValidator;
import org.middleheaven.validation.SimpleEmailAddressValidator;

public class AnnotatedBeanValidator<T> extends BeanValidator<T>{

	
	@SuppressWarnings("unchecked")
	public static <U> AnnotatedBeanValidator<U> getInstanceFor(EntityModel model){
		AnnotatedBeanValidator<U> validator = new AnnotatedBeanValidator<U>();
		
		for (EntityFieldModel efm : model.fields()){
			CompositeValidator<?> comp = new CompositeValidator();
			if (!efm.isNullable()){
				comp.add(new NotNullValidator());
			}
			
			if (efm.getDataType().equals(DataType.TEXT)){
				TextDataTypeModel dtModel = (TextDataTypeModel)efm.getDataTypeModel();
				
				if (!dtModel.isEmptyable()){
					comp.add(new NotEmptyValidator());
				} 
				
				if (dtModel.isEmailAddress()){
					comp.add(new SimpleEmailAddressValidator());
				} 
				
				
				comp.add(new LengthValidator(dtModel.getMinLength(), dtModel.getMaxLength()));
				
			} 
			
			validator.addPropertyValidator(efm.getName().getDesignation(), comp);
		}
		return validator;
	}
	
	private AnnotatedBeanValidator(){
		
	}
}
