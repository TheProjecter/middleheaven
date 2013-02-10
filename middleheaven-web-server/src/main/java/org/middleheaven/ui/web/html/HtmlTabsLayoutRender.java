/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of a Layout.
 */
public class HtmlTabsLayoutRender extends AbstractHtmlRender {


	private static final long serialVersionUID = 4486173553150482265L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
		
		Writer body = document.getBodyWriter();
		
		String[] famillyParams =  component.getFamily().split(":");
		
		String orientation = "horizontal";
		
		if (famillyParams.length > 1 && "vertical".equals(famillyParams[1])){
			orientation = "vertical";
		} 
			
			document.addRelativeStylesheet("css/ui/jquery-ui-1.8.20.custom.css");
			document.addRelativeStylesheet("css/ui/extention.css");
			
			HtmlScript jquery = new HtmlScript("jquery");
			jquery.setRelativeSource("js/jquery-1.7.2.min.js");
			document.addScript(jquery);
			
			HtmlScript jqueryUI = new HtmlScript("jquery-ui");
			jqueryUI.setRelativeSource("js/jquery-ui-1.8.20.custom.min.js");
			document.addScript(jqueryUI);
			
			
			HtmlScript script = new HtmlScript("jquery-tabs-init");
			script.append("$(function() {$( \"#").append(component.getGID()).append("\" ).tabs();});");
			
			document.addScript(script);
			
			// horizontal or default
			body.append("<div  ");
			body.append(" class=\"mh-ui-layout-tabs-").append(orientation).append("\"");
			body.append(" id=\"").append(component.getGID()).append("\"");
			body.append(" uiType=\"").append("layout").append("\"");
			
			body.append(">");
			// render children
			
			
		
			body.append("<ul>");
			int count= 1;
			for (UIComponent c : component.getChildrenComponents()){
				
				String title = "tab " + Integer.toString(count+1);
				
				if (c.isType(UIView.class)) {
					UIView view = (UIView) c;
					
					final TextLocalizable textLocalizable = view.getTitleProperty().get();
					if (textLocalizable != null){
						title = this.localize(textLocalizable, document.getCulture());
					}
				}
				
				body.append("<li><a href=\"#").append(component.getGID()).append("-").append(Integer.toString(count++)).append("\">").append(title).append("</a></li>");
				
			}
			body.append("</ul>");

			count= 1;
			for (UIComponent c : component.getChildrenComponents()){
				body.append("<div  ");
				body.append(" id=\"").append(component.getGID()).append("-").append(Integer.toString(count++)).append("\"");
				body.append(">");
				
				((GenericHtmlUIComponent) c).writeTo(document, context);
				body.append("</div>");
			}
	
		
			
			body.append("</div>");
		

	}

}
