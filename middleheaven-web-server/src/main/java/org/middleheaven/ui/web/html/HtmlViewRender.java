/**
 * 
 */
package org.middleheaven.ui.web.html;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class HtmlViewRender extends AbstractHtmlRender {


	private static final long serialVersionUID = 4486173553150482265L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
		
		
		UIView view =  safeCast(component, UIView.class).get();

		Writer writer = document.getBodyWriter();
		
		writer.append("<div  ");
		writer.append(" class=\"mh-ui-view\"" );
		writer.append(" id=\"").append(component.getGID()).append("\"");
		writer.append(" uiType=\"").append("view").append("\"");
		
		writer.append(">");
		// render children
		
		for (UIComponent c : component.getChildrenComponents()){
			((GenericHtmlUIComponent) c).writeTo(document, context);
		}
		
	
		
		writer.append("</div>");

	}

}
