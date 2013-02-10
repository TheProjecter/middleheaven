package org.middleheaven.domain.model;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.model.AbstractEditableModelSet;
import org.middleheaven.util.function.Maybe;

/**
 * 
 */
public class EditableDomainModel extends AbstractEditableModelSet<EntityModel> implements DomainModel {

	private Map<String, EnumModel> enumModels = new HashMap<String, EnumModel>();
	
	/**
	 * 
	 */
	public EditableDomainModel(){}

	@Override
	public <E> Maybe<EnumModel> getEmumModel(Class<E> type) {
		return Maybe.of(enumModels.get(type.getName()));
	}

	public void addEnumModel (EnumModel model){
		enumModels.put(model.getEnumType().getName(), model);
	}

}
