/**
 * 
 */
package org.middleheaven.ui.components;

import org.middleheaven.util.property.Property;


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
