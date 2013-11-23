/*
 * Created on 2006/10/15
 *
 */
package org.middleheaven.global.text;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.culture.Culture;
import org.middleheaven.global.LocalizationService;


/**
 * Formats a reference <code>String</code> by translating it into a global displayable <code>String</code>
 * making use of the <code>LocalizationService</code>
 * 
 * 
 * @see org.middleheaven.global.LocalizationService
 */
public class GlobalFormatter implements Formatter<String> {

	
	private Culture culture;

	public GlobalFormatter (Culture culture){
		this.culture = culture;
	}
	
	public String format(String object) {
		return ServiceRegistry.getService(LocalizationService.class).getMessage( LocalizableText.valueOf(object) , culture);

	}

	public String parse(String stringValue) {
		return stringValue;
	}

}
