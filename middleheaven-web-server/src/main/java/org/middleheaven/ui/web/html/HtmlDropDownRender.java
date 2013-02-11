package org.middleheaven.ui.web.html;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.data.UIDataItem;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of a Dropdown.
 */
public class HtmlDropDownRender extends AbstractHtmlInputRender {

	private static final long serialVersionUID = 1579436461994433298L;

	@Override
	public void write(HtmlDocument document, RenderingContext context,UIComponent component) throws IOException {
		
		UISelectOne selectOne = safeCast(component, UISelectOne.class).get();
		
		Writer writer = document.getBodyWriter();
		
		
		writer.append("<select ")
		.append(" id=\"" + component.getGID() + "\"")
		.append(" name=\"" + selectOne.getNameProperty().get() + "\"")
		.append(" class=\"mh-ui-select-one\"" )
		.append(" uiType=\"").append("select-one").append("\"")
		.append(">");
		
		for (int i=0; i < selectOne.getDataSize();i++){
			
			UIDataItem element = selectOne.getElementAt(i);
			writer.append("<option value=\"")
			.append(element.toString())
			.append("\" ");
			
			if(selectOne.isSelectedIndex(i)){
				writer.append(" selected=\"selected\"");
			}
			
			writer
			.append(" >")
			.append(selectOne.getFormaterProperty().get().format(element.get(0)))
			.append("</option>");
			
			
		}
		
		
		writer.append("</select>");
	}

}
