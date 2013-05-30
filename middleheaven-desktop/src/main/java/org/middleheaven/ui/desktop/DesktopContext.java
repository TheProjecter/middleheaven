/**
 * 
 */
package org.middleheaven.ui.desktop;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.MapContext;

/**
 * 
 */
public class DesktopContext {

	private static final DesktopContext ME = new DesktopContext(); 
	
	public static DesktopContext getDesktopContext(){
		return ME;
	}
	
	private AttributeContext attributeContext = new MapContext();
	
	public AttributeContext getAttributeContext(){
		return attributeContext;
	}
}
