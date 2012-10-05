package org.middleheaven.ui.testui;

import org.middleheaven.ui.GenericUIComponent;
import org.middleheaven.ui.UIComponent;

/**
 * This component can be used to test proposes.
 * Is offers no graphic interface thus being suitable for a off screen 
 * test. 
 * 
 */
public class TestUIComponent<T extends UIComponent> extends GenericUIComponent<T> {



	public TestUIComponent(Class<T> renderType, String familly) {
		super(renderType, familly);
	}

	@Override
	public boolean isRendered() {
		return true;
	}
	

}
