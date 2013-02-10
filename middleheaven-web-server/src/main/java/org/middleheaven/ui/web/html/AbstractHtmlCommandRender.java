/**
 * 
 */
package org.middleheaven.ui.web.html;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.ui.UIComponent;
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
		
		GenericHtmlUIComponent uic = new GenericHtmlUIComponent(component, this);
	 
		HtmlUICommand ruic =   Introspector.of(uic.getComponentType()).newProxyInstance(new HTMLUIComponentProxyHandler(uic), HtmlUICommand.class);
		
		ruic.setUIParent(parent);

		init(ruic);
		
		return ruic;
	}

}
