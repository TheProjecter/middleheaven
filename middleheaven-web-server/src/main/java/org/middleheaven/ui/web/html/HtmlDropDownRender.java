package org.middleheaven.ui.web.html;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;
import java.io.Writer;

import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UISelectOne;
import org.middleheaven.ui.data.UIDataItem;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * Html implementation of a Dropdown.
 */
public class HtmlDropDownRender extends AbstractHtmlInputRender {

	private static final long serialVersionUID = 1579436461994433298L;

	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		HtmlUIComponent ruic =  new HtmlUISelectOneImpl((UISelectOne)component, this);

		ruic.setUIParent(parent);

		init(ruic);
		
		return ruic;
	}
	
	
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
			.append(element.get(1).toString())
			.append("\" ");
			
			if(selectOne.isSelectedIndex(i)){
				writer.append(" selected=\"selected\"");
			}
			
			ParsableFormatter formatter = selectOne.getFormaterProperty().get();
			writer
			.append(" >")
			.append(formatter == null ? element.get(0).toString() : formatter.format(element.get(0)))
			.append("</option>");
			
			
		}
		
		
		writer.append("</select>");
	}

}
