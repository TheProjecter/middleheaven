package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.rendering.RenderingContext;

public class SSplashWindowRender extends SwingUIRender {

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {

		UIWindow window =  new SRawWindow();

		return window;
	}

}
