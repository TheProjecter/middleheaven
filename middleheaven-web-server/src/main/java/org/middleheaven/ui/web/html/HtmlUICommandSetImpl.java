/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;

import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class HtmlUICommandSetImpl extends GenericUIComponent<UICommandSet> implements HtmlUIComponent , UICommandSet{

	private AbstractHtmlRender render;
	
	/**
	 * Constructor.
	 * @param component
	 * @param abstractHtmlCommandRender
	 */
	public HtmlUICommandSetImpl(UICommandSet component, AbstractHtmlRender render) {
		super(UICommandSet.class, component.getFamily());
		this.render = render;

		this.getVisibleProperty().set(component.getVisibleProperty().get());
		this.getEnableProperty().set(component.getEnableProperty().get());
	}
	
	@Override
	public boolean isRendered() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeTo(HtmlDocument doc, RenderingContext context) throws IOException {
		 this.render.write(doc, context, this);
	}

}
