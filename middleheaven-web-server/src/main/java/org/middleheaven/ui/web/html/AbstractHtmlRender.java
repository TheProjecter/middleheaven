package org.middleheaven.ui.web.html;

import java.io.IOException;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.global.Culture;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

/**
 * Abstract {@link UIRender} for the HTML display.
 * 
 * 
 */
public abstract class AbstractHtmlRender extends UIRender{


	private static final long serialVersionUID = 2919032845952457519L;

	/**
	 * @param text
	 * @param context
	 * @return
	 */
	protected final String localize(LocalizableText text, Culture culture) {
		
		if (text == null){
			return "null";
		}
		if (text.isLocalized()){
			return text.toString();
		}
		return ServiceRegistry.getService(LocalizationService.class).getMessage(text, culture);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		GenericHtmlUIComponent uic = new GenericHtmlUIComponent(component, this);
	 
		HtmlUIComponent ruic =   Introspector.of(uic.getComponentType()).newProxyInstance(new HTMLUIComponentProxyHandler(uic), HtmlUIComponent.class);
		
		ruic.setUIParent(parent);

		init(ruic);
		
		return ruic;
	}
	
	/**
	 * @param ruic
	 */
	protected void init(UIComponent component) {
	}

	/**
	 * the {@link AbstractHtmlRender} utilizes a single type for any html component that delegates back to its 
	 * render for the actual HTML wiring.
	 * 
	 * @param document the document to where to write HTML
	 * @param context the current {@link RenderingContext}.
	 * @param component the component being rendered.
	 * @throws IOException if any problem occurs while writing.
	 */
	protected abstract void write(HtmlDocument document, RenderingContext context , UIComponent component) throws IOException;


	

}
