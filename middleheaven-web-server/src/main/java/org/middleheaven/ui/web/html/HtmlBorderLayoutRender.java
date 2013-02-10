/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.layout.UIBorderLayoutConstraint;
import org.middleheaven.ui.layout.UIBorderLayoutManager;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of a Layout.
 */
public class HtmlBorderLayoutRender extends AbstractHtmlRender {


	private static final long serialVersionUID = 4486173553150482265L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {

		Writer body = document.getBodyWriter();

		final UILayout uiLayout = (UILayout)component;
		UIBorderLayoutManager layoutManager = (UIBorderLayoutManager) uiLayout.getLayoutManager();


		Map<String, UIComponent> mapping = new HashMap<String, UIComponent>();

		for (UIComponent c : uiLayout.getChildrenComponents()){
			mapping.put(c.getGID(), c);
		}

		UIComponent north = mapping.get(layoutManager.getBorderComponentId(UIBorderLayoutConstraint.NORTH));
		UIComponent west = mapping.get(layoutManager.getBorderComponentId(UIBorderLayoutConstraint.WEST));
		UIComponent east = mapping.get(layoutManager.getBorderComponentId(UIBorderLayoutConstraint.EAST));
		UIComponent center = mapping.get(layoutManager.getBorderComponentId(UIBorderLayoutConstraint.CENTER));
		UIComponent south = mapping.get(layoutManager.getBorderComponentId(UIBorderLayoutConstraint.SOUTH));


		// horizontal or default
		body.append("<div  ");
		body.append(" class=\"mh-ui-layout-border\"");
		body.append(" id=\"").append(component.getGID()).append("\"");
		body.append(" uiType=\"").append("layout").append("\"");

		body.append(">");
		// render children

		// north row

		body.append("<div class=\"mh-ui-layout-border-north\">");
		if (north != null){
			((GenericHtmlUIComponent) north).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");

		// center row

		body.append("<div class=\"mh-ui-layout-border-center-pane\" >");

		// west
		body.append("<div  class=\"mh-ui-layout-border-west\">");
		if (west != null) {
			((GenericHtmlUIComponent) west).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");

		// center
		body.append("<div  class=\"mh-ui-layout-border-center\">");

		if (center != null) {
			((GenericHtmlUIComponent) center).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");


		// east

		body.append("<div  class=\"mh-ui-layout-border-east\">");
		if (east != null) {
			((GenericHtmlUIComponent) east).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");


		body.append("</div>");



		// south  row

		body.append("<div class=\"mh-ui-layout-border-south\">");
		if (south != null){
			((GenericHtmlUIComponent) south).writeTo(document, context);
		} else {
			body.append("&nbsp;");
		}
		body.append("</div>");

		body.append("</div>");


	}


}
