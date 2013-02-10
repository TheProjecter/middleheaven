/**
 * 
 */
package org.middleheaven.ui.web.html;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIField;

/**
 * 
 */
public abstract class AbstractHtmlInputRender extends AbstractHtmlRender{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2623390639909205306L;

	/**
	 * 
	 * @param component
	 */
	@Override
	protected void init(UIComponent component) {
		UIField comp = (UIField)component;
		comp.getReadStateProperty().set(UIReadState.INPUT_ENABLED);
	}

}
