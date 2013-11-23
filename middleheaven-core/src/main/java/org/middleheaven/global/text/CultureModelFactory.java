/**
 * 
 */
package org.middleheaven.global.text;

import java.util.Collection;

import org.middleheaven.culture.Culture;


/**
 * 
 */
public interface CultureModelFactory {

	public Collection<Culture> getSupportedCultures();
	   
	public CultureModel resolveModelFor(Culture culture);
}
