/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

import com.vaadin.ui.TextField;

/**
 * 
 */
public class VaadinTextInputRender extends AbstractVaadinRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7599970618804979823L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,UIComponent component) {
	
		 TextField editor = new TextField();

		return new VaadinTextInput(editor);
	}

}
