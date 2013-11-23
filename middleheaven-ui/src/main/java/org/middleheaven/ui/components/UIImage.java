/**
 * 
 */
package org.middleheaven.ui.components;

import org.middleheaven.ui.property.Property;


/**
 * The ouput for an image.
 */
public interface UIImage extends UIOutput {

	/**
	 * 
	 * @return
	 */
	public Property<String> getImageNameProperty();
	
}
