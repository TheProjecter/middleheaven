/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.components.UICommandSet;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.property.Property;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

/**
 * 
 */
public class VaadinCommandSet extends VaadinUIComponentContainer implements UICommandSet {

	/**
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinCommandSet() {
		super(new Panel(new HorizontalLayout()), UICommandSet.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		
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
	public Property<LocalizableText> getTextProperty() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<String> getNameProperty() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCommandListener(CommandListener listener) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCommandListener(CommandListener listener) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<CommandListener> getCommandListeners() {
		throw new UnsupportedOperationException("Not implememented yet");
	}
}
