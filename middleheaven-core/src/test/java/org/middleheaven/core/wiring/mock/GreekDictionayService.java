/**
 * 
 */
package org.middleheaven.core.wiring.mock;

import org.middleheaven.core.annotations.Profile;

/**
 * 
 */
@Profile("production")
public class GreekDictionayService extends HashDictionaryService {
	
	/**
	 * Constructor.
	 * @param lang
	 */
	public GreekDictionayService() {
		super("gr");
	}

}
