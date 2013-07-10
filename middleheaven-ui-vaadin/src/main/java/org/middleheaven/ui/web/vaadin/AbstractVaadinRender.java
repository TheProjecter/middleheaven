/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

/**
 * 
 */
public abstract class AbstractVaadinRender extends UIRender {

	
	/**
	 * @param text
	 * @param locale 
	 * @param context
	 * @return
	 */
	protected final String localize(LocalizableText text, Culture culture) {
		if (text.isLocalized()){
			return text.toString();
		}
		return ServiceRegistry.getService(LocalizationService.class).getMessage(text, culture);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected final UIComponent build(RenderingContext context, UIComponent parent,
			UIComponent component) {
		
		VaadinUIComponent c = buildVaadin(context,parent,component);

		c.setUIParent(parent);
		c.getComponent().setSizeFull();
		return c;
	}

	protected abstract VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent, UIComponent component);
	
}
