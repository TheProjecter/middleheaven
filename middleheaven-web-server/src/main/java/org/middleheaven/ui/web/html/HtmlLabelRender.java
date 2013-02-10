/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of a Label.
 */
public class HtmlLabelRender extends AbstractHtmlRender {


	private static final long serialVersionUID = 4486173553150482265L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
		
		
		UILabel label = (UILabel) component;
		Writer writer = document.getBodyWriter();
		
		writer.append("<label  ")
		.append(" class=\"mh-ui-label\"" )
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(">")
		.append(this.localize(label.getTextProperty().get(), document.getCulture()))
		.append("</label>");

	}

}
