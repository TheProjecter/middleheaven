package org.middleheaven.ui.desktop.swing;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIColorField;
import org.middleheaven.ui.components.UIDateField;
import org.middleheaven.ui.components.UINumericField;
import org.middleheaven.ui.components.UISecretField;
import org.middleheaven.ui.rendering.RenderingContext;

public class SFieldRender  extends SwingUIRender{

	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent,UIComponent component) {

		if (UINumericField.class.isAssignableFrom(component.getComponentType()) || 
				"numeric".equals(component.getFamily())){
			return new SNumericFieldInput();
		} else if (UISecretField.class.isAssignableFrom(component.getComponentType()) || 
				"secret".equals(component.getFamily())){
			return new SSecretInput();
		} else if (UIDateField.class.isAssignableFrom(component.getComponentType()) || 
				"date".equals(component.getFamily())){
			return new SDateFieldInput();
		} else if (UIColorField.class.isAssignableFrom(component.getComponentType()) || 
				"color".equals(component.getFamily())){
			return new SColorFieldInput();
		} else {
			return new STextFieldInput();
		}
	}

}
