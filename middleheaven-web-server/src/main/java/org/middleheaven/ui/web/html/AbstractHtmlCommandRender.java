/**
 * 
 */
package org.middleheaven.ui.web.html;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.rendering.RenderingContext;


/**
 * 
 */
public abstract class AbstractHtmlCommandRender extends AbstractHtmlRender {

	private static final long serialVersionUID = -9059264594730967204L;

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		HtmlUIComponent ruic =  new HtmlUICommandImpl((UICommand)component, this);

		ruic.setUIParent(parent);

		init(ruic);
		
		return ruic;
	}

}
