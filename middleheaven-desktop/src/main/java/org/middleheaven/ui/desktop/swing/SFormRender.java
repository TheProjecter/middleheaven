package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.rendering.RenderingContext;

/**
 * A swing form Render
 */
public class SFormRender extends SwingUIRender {

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		
		UILayout layout =  context.getRenderKit().renderComponent(context, parent, 
				GenericUIComponent.getInstance(UILayout.class, "box") );
		UICommandSet set = context.getRenderKit().renderComponent(context, parent, 
				GenericUIComponent.getInstance(UICommandSet.class, "toolbar") ); 
			
		return new SForm(set, layout);
	}

}
