package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;

public class SMenuRender extends SwingUIRender {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isChildrenRenderer(RenderingContext context, UIComponent parent,UIComponent component) {
		return true;
	}
	
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {
		
		UICommandSet menu = parent.getComponentType().equals(UICommandSet.class) ? 
			new SMenu() : 
			new SMenuBar();

		menu.setFamily("menu");
		
		RenderKit renderKit = context.getRenderKit();
		for (UIComponent comp : component.getChildrenComponents()){

			UIComponent renderedComponent = renderKit.renderComponent(context, menu, comp);

			menu.addComponent(renderedComponent);
		}
		
		return menu;
		
	}

}
