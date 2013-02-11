/**
 * 
 */
package org.middleheaven.ui.web.html;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;
import java.io.Writer;

import javax.swing.JComponent;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * 
 */
public class HtmlWindowRender extends AbstractHtmlRender  {


	private static final long serialVersionUID = 589975837582085054L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
	
		UIWindow window = safeCast(component, UIWindow.class).get(); 
		
		Writer head = document.getHeadWriter();
		
		if (window.getTitleProperty().get() != null){
			head.append(" <title>").append(this.localize(window.getTitleProperty().get(), document.getCulture())).append("</title>");
		}
		
		Writer body = document.getBodyWriter();
		
		body.append("<div  ")
		.append(" class=\"mh-ui-window\"")
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(" uiType=\"").append("window").append("\"");
		
		body.append(">");
		
		// render children
		
		for (UIComponent c : component.getChildrenComponents()){
			((GenericHtmlUIComponent) c).writeTo(document, context);
		}
		
		
		body.append("</div>");

	}


}
