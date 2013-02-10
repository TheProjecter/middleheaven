/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIImage;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of an Image.
 */
public class HtmlImageRender extends AbstractHtmlRender {


	private static final long serialVersionUID = 4486173553150482265L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
		
		
		UIImage image = (UIImage) component;
		Writer writer = document.getBodyWriter();
		
		writer.append("<img  ")
		.append(" class=\"mh-ui-image\"" )
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(" src=\"").append(document.getContextPath()).append("/images/").append(image.getImageNameProperty().get()).append("\"")
		.append("/>");

	}

}
