package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Implementation of a HiperLink Command in HTML
 */
public class HtmlCommandHiperLinkRender extends AbstractHtmlCommandRender {


	private static final long serialVersionUID = 7846444730437079493L;

	@Override
	public void write(HtmlDocument document, RenderingContext context,UIComponent component) throws IOException {
		
		UICommand command = (UICommand) component;
		
		Writer writer = document.getBodyWriter();
		
		writer.append("<a ")
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(" class=\"mh-ui-comand-link\"" )
		.append(" uiType=\"").append("command-link").append("\"");
		
		writer.append(" onClick=\"javascript:submit()\"")
		.append(">")
		.append(localize(command.getTextProperty().get(), document.getCulture()))
		.append("</a>");
	}





}
