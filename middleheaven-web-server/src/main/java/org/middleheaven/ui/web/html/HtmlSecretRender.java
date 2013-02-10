package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.rendering.RenderingContext;

public class HtmlSecretRender extends AbstractHtmlInputRender {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 * @param b
	 */
	public HtmlSecretRender( ) {

	}

	@Override
	public void write(HtmlDocument document, RenderingContext context,UIComponent component) throws IOException {

		UIField comp = (UIField)component;


		UIReadState state = comp.getReadStateProperty().get();

		if (state == null){
			state = UIReadState.INPUT_ENABLED;
			
		}
		Writer writer = document.getBodyWriter();
		
		String value = comp.getValueProperty().get() == null ? "" : comp.getValueProperty().get().toString();  
		final String name = comp.getNameProperty().get();
		
		if (!state.isVisible()){
			writer.append("<input ")
			.append(" type=\"hidden\"")
			.append(" id=\"" + component.getGID() + "\"")
			.append(" name=\"" + name + "\"")
			.append(" value=\"" + value + "\"")
			.append(" type=\"hidden\"")
			.append(" class=\"mh-ui-input\"" )
			.append(" uiType=\"").append("input").append("\"")
			.append("/>");
		} else if (!state.isEditable()){
			writer.append("<input ");
			writer.append(" id=\"" + component.getGID() + "\"");
			writer.append(" name=\"" + name + "\"");
			writer.append(" value=\"" + value + "\"");
			writer.append(" type=\"hidden\"");
			writer.append("/>");
			writer.append("<span class=\"readOnlyField\"");
			writer.append(" id=\"" + component.getGID() + "\"")
			.append(" class=\"mh-ui-input\"" )
			.append(" uiType=\"").append("input").append("\"")
			.append(">")
			.append(value)
			.append("</span>");
		} else {
			writer.append("<input ");
			writer.append(" id=\"" + component.getGID() + "\"");
			writer.append(" name=\"" + name + "\"");
			writer.append(" value=\"" + value + "\"")
			.append(" type=\"password\"")
			.append(" class=\"mh-ui-input\"" )
			.append(" uiType=\"").append("input").append("\"");
			
			if (!state.isEnabled()){
				writer.append(" disabled=\"disabled\" ");
			}
			
			final Integer maxLength = comp.getMaxLengthProperty().get();
			if (maxLength  > 0 ){
				writer.append(" maxlength=\"" + maxLength + "\"");
			}
			
			writer.append("/>");
		}

		
	

		


		



	}

}
