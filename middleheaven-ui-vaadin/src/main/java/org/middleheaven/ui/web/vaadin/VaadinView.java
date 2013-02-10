/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIPrespectiveListener;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.util.property.Property;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;

/**
 * 
 */
public class VaadinView extends VaadinUIComponentContainer implements UIView  {

	private final EventListenersSet<UIPrespectiveListener> listeners = EventListenersSet.newSet(UIPrespectiveListener.class);
	
	private final Property<TextLocalizable> title = VTitlePropery.bind(this);
	
	/**
	 * Constructor.
	 */
	public VaadinView() {
		super(new Panel(), UIView.class);
	
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
	public Property<TextLocalizable> getTitleProperty() {
		return title;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPrespectiveListener(UIPrespectiveListener listener) {
		listeners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePrespectiveListener(UIPrespectiveListener listener) {
		listeners.removeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<UIPrespectiveListener> getPrecpectiveListeners() {
		return listeners;
	}

	
}
