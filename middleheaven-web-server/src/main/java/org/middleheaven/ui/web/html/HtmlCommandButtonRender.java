package org.middleheaven.ui.web.html;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Implementation of a Button Command in HTML.
 */
public class HtmlCommandButtonRender extends AbstractHtmlCommandRender {


	private static final long serialVersionUID = -1218761349031119712L;

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void write(HtmlDocument document, RenderingContext context, UIComponent component) throws IOException {
		
		UICommand command = safeCast(component, UICommand.class).get();
		
		document.addRelativeStylesheet("css/ui/jquery-ui-1.8.20.custom.css");
		document.addRelativeStylesheet("css/ui/extention.css");
		
		HtmlScript jquery = new HtmlScript("jquery");
		jquery.setRelativeSource("js/jquery-1.7.2.min.js");
		document.addScript(jquery);
		
		HtmlScript jqueryUI = new HtmlScript("jquery-ui");
		jqueryUI.setRelativeSource("js/jquery-ui-1.8.20.custom.min.js");
		document.addScript(jqueryUI);
		
		document.addScript(new HtmlScript("mh-ui-exec").setRelativeSource("js/mh-ui-exec.js"));
		
	
		Writer writer = document.getBodyWriter();
		
		writer.append("<input  ")
		.append(" id=\"").append(component.getGID()).append("\"")
		.append(" name=\"").append(command.getNameProperty().get()).append("\"")
		.append(" class=\"mh-ui-command\"" )
		.append(" uiType=\"").append("command").append("\"");
		
		writer.append(" type=\"submit\" ");
		
		
		if(!component.getEnableProperty().get()){
			writer.append(" disabled=\"disabled\" ");
		}

		writer.append(" value=\"" + this.localize(command.getTextProperty().get() , document.getCulture()) + "\" />");

	}

}
