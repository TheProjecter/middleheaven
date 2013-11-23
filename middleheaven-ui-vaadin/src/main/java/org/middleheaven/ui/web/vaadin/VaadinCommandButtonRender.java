/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.util.SafeCastUtils;

import com.vaadin.ui.Button;

/**
 * 
 */
public class VaadinCommandButtonRender extends AbstractVaadinRender {


	private static final long serialVersionUID = -6503686577233772775L;

	public VaadinCommandButtonRender (){
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected VaadinUIComponent buildVaadin(RenderingContext context, UIComponent parent,
			UIComponent component) {
		

		UICommand command = SafeCastUtils.safeCast(component, UICommand.class).get();

		VaadinButton vButton = new VaadinButton(new Button());
		vButton.setUIParent(parent);
		
		for (CommandListener c : command.getCommandListeners()){
			vButton.addCommandListener(c);
		}
		
		vButton.getNameProperty().set(command.getNameProperty().get());
		vButton.getTextProperty().set(command.getTextProperty().get());
		
		return vButton;

	}

}
