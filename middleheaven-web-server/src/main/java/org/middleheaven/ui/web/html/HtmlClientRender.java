/**
 * 
 */
package org.middleheaven.ui.web.html;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;
import org.middleheaven.ui.web.Browser;

/**
 * 
 */
public class HtmlClientRender extends UIRender {


	private static final long serialVersionUID = 3124723160412077219L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		Browser browser =  new Browser(context.getRenderKit().getSceneNavigator());
		
		browser.setUIParent(parent);

		return browser;
	}

}
