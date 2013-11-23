/**
 * 
 */
package org.middleheaven.global.text;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.culture.Culture;

/**
 * 
 */
class HashCultureModelFactory implements CultureModelFactory {

	
	private Map<Culture, CultureModel> models = new HashMap<Culture, CultureModel>();
	
	public HashCultureModelFactory (){
		
	}
	
	public void addModel(Culture culture, CultureModel model){
		this.models.put(culture, model);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CultureModel resolveModelFor(Culture culture) {
		return models.get(culture);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Culture> getSupportedCultures() {
		return Collections.unmodifiableCollection(models.keySet());
	}

}
