/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.global.Culture;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of an Image.
 */
public class HtmlFormRender extends AbstractHtmlRender {


	private static final long serialVersionUID = 4486173553150482265L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {

		Writer writer = document.getBodyWriter();

		writer.append("<form  ")
		.append(" class=\"mh-ui-form\"" )
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(">");

		// render children

		writer.append("<fieldset>");
		writer.append("<table>");
		for (UIComponent c : component.getChildrenComponents()){
			writer.append("<tr>");
			GenericHtmlUIComponent field = ((GenericHtmlUIComponent) c);
			
			writer.append("<td class=\"mh-ui-form-label-column\">");
			if (c instanceof UIField){
				String name = ((UIField) c).getNameProperty().get();
				final Culture culture = document.getCulture();
				writer.append("<label class=\"mh-ui-label\" lang=\"").append(culture.getLanguage().toString())
				.append(" for=\"").append(c.getGID()).append("\" >")
				.append(this.localize(LocalizableText.valueOf("ui:label.field." + name), culture))
				.append("</label>:");
				
			}
			writer.append("</td>");
			writer.append("<td class=\"mh-ui-form-input-column\">");
			field.writeTo(document, context);
			writer.append("</td>");
			writer.append("</tr>");
		}
		writer.append("</table>");
		writer.append("</fieldset>");
		writer.append("</form>");


	}

}
