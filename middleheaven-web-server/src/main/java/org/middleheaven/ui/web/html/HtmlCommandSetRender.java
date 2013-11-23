package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * HTML representation of a simple command set.
 */
public class HtmlCommandSetRender extends AbstractHtmlRender {

	private static final long serialVersionUID = -8973921831217802238L;

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		HtmlUIComponent ruic =  new HtmlUICommandSetImpl((UICommandSet)component, this);

		ruic.setUIParent(parent);

		init(ruic);
		
		return ruic;
	}
	
	@Override
	public void write(HtmlDocument document, RenderingContext context,
			UIComponent component) throws IOException {

		Writer writer = document.getBodyWriter();
		
		writer.append("<div ");
		writer.append(" id=\"" + component.getGID() + "\"")
		.append(" class=\"mh-ui-command-set\"" )
		.append(" uiType=\"").append("command-set").append("\"")
		.append(">");
		
		for (UIComponent c : component.getChildrenComponents()){
			((HtmlUIComponent) c).writeTo(document, context);
		}
		
		writer.append("</div>");
	}

}
