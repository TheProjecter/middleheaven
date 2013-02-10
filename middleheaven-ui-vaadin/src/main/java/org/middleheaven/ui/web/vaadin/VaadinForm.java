/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.util.Collections;
import java.util.List;

import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UIForm;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.models.form.UIFormSheetModel;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;

/**
 * 
 */
public class VaadinForm extends VaadinUIComponentContainer implements UIForm{

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinForm() {
		super(new Panel(new FormLayout()), UIForm.class);
	}

	public VaadinForm(Panel panel) {
		super(panel, UIForm.class);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UICommandSet getCommandSet() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		this.getComponent().setSizeFull();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout layout) {
		this.layout = layout;
		
		if (layout instanceof VaadinUIComponent){
			Panel component = (Panel) this.getComponent();
			component.setContent((ComponentContainer) ((VaadinUIComponent)layout).getComponent());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UIFormSheetModel> getFormSheets() {
		return Collections.emptyList();
	}

}
