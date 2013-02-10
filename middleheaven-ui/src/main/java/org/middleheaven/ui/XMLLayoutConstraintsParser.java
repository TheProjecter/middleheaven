/**
 * 
 */
package org.middleheaven.ui;

import org.middleheaven.ui.layout.UIBorderLayoutConstraint;

/**
 * 
 */
public class XMLLayoutConstraintsParser {

	
	public static UILayoutConstraint parseConstraint(String layoutConstraint, String layoutFamilly) {
		
		
		if ("border".equalsIgnoreCase(layoutFamilly)) {
			return UIBorderLayoutConstraint.valueOf(layoutConstraint.toUpperCase());
		} else {
			throw new IllegalArgumentException("'" + layoutConstraint + "' is not a valid constraint for layout familly " + layoutFamilly);
		}
	}
}
