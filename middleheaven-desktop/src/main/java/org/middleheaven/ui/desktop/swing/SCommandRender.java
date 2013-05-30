package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;

public class SCommandRender extends SwingUIRender {

	private static final long serialVersionUID = 1L;

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {
	
		if ("menu".equals(parent.getFamily())){
			if (parent instanceof SMenu){
				return new SMenuButton();
			} else {
				return new SMenu();
			}
		} else {
			return new SButton();
		}
	}

}
