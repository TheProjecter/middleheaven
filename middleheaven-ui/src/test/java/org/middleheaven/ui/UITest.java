package org.middleheaven.ui;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.middleheaven.tool.test.MiddleHeavenTestCase;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.testui.TestRenderKit;



public class UITest extends MiddleHeavenTestCase{
	
	@Test
	public void testRendering(){
		
		UIClient root =  GenericUIComponent.getInstance(UIClient.class, null);
		
		UIView frame = GenericUIComponent.getInstance(UIView.class, null);
		
		root.addComponent(frame, null);

		TestRenderKit rk = new TestRenderKit();
		RenderingContext context = new RenderingContext(rk);
		
		UIComponent renderedUIC = rk.renderComponent(context,null, root);
		
		assertTrue (renderedUIC.isRendered());
		assertTrue (renderedUIC instanceof  UIClient);
		
	}


}
