/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.rendering.RenderingContext;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 */
public class VaddinFormRender extends AbstractVaadinRender {

	private static final long serialVersionUID = 779009648225234413L;


	/**
	 * Is this renderer responsible for rendering children components
	 * @return
	 */
	public boolean isChildrenRenderer(RenderingContext context, UIComponent parent,UIComponent component) {
		return true;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context,UIComponent parent, UIComponent component) {
		
		VaadinForm form = new VaadinForm();
		form.setUIParent(parent);
	
		for (UIComponent c : component.getChildrenComponents()){
			
			if (c.isType(UICommandSet.class)){
				VaadinUIComponent v = (VaadinUIComponent) context.getRenderKit().renderComponent(context, form, c);
				
				form.getComponent().addComponent(v.getComponent());
				
				form.addComponent(v);
			} else {
				String name = ((UIField)c).getNameProperty().get();
				
				HorizontalLayout h = new HorizontalLayout();
				
				VaadinUIComponent v = (VaadinUIComponent) context.getRenderKit().renderComponent(context, form, c);
				
				h.addComponent(new Label(localize(LocalizableText.valueOf("ui:label.field." + name), ((VaadinUIComponent) parent).getCulture()))); // TODO locale
				h.addComponent(v.getComponent()); 
				
				form.getComponent().addComponent(h);
				
				form.addComponent(v);
			}
			
		}
		
		
		return form;
	}

}
