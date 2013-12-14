package org.middleheaven.ui.testui;

import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

public class TestUIRender extends UIRender {

	private static final long serialVersionUID = 1L;

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		Class<UIComponent> type = component.getComponentType();
		
		TestUIComponent t=  new TestUIComponent(component.getComponentType(), component.getFamily());

		t.setGID(component.getGID());
		t.setUIParent(parent);

		return Introspector.of(type).newProxyInstance(new TestUIProxyHandler(t));
	}

}
