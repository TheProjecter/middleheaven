/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public interface HtmlUIComponent extends UIComponent, UIContainer {

	public abstract void writeTo(HtmlDocument doc, RenderingContext context) throws IOException;

}