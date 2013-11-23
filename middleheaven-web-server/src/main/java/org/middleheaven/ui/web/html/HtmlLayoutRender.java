/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of a Layout.
 */
public class HtmlLayoutRender extends AbstractHtmlRender {


	private static final long serialVersionUID = 4486173553150482265L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
		
		Writer writer = document.getBodyWriter();
		
		writer.append("<div  ")
		.append(" class=\"mh-ui-layout\"")
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(" uiType=\"").append("layout").append("\"")
		.append(">");
		
		// render children
		
		for (UIComponent c : component.getChildrenComponents()){
			writer.append("<div  ")
			.append(" class=\"mh-ui-layout-pane\"")
			.append(">");
			
			((GenericHtmlUIComponent) c).writeTo(document, context);
			
			writer.append("</div>");
		}

		writer.append("</div>");

	}

}
