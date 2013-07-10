/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class VaadinCommandSetRender extends AbstractVaadinRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 445515222317732514L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {


		VaadinCommandSet set = new VaadinCommandSet();
		set.setUIParent(parent);
		
		return set;
	}

}
