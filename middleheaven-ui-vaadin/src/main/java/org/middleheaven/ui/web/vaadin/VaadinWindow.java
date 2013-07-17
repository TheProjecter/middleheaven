/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.util.Locale;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIPrespectiveListener;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.components.UIWindowsListener;
import org.middleheaven.util.property.Property;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;

/**
 * 
 */
public class VaadinWindow extends VaadinUIComponentContainer implements UIWindow {
	
	private final EventListenersSet<UIPrespectiveListener> listeners = EventListenersSet.newSet(UIPrespectiveListener.class);
	private final EventListenersSet<UIWindowsListener> windowlisteners = EventListenersSet.newSet(UIWindowsListener.class);
	
	private final Property<LocalizableText> title = VTitlePropery.bind(this);

	/**
	 * Constructor.
	 * @param locale 
	 */
	public VaadinWindow(Locale locale) {
		super(new Panel(), UIWindow.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void copyModel() {
		
	}
	
	// Remodel Window implementation as a VaadinView
//	public void setVisible(boolean visible){
//		
//		super.setVisible(visible);
//		
//		Window component = (Window) this.getComponent();
//		
//		Application app = component.getApplication();
//		
//		app.setMainWindow(component);
//	}

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
	public Property<LocalizableText> getTitleProperty() {
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
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addUIWindowListener(UIWindowsListener listener) {
		windowlisteners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUIWindowListener(UIWindowsListener listener) {
		windowlisteners.removeListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<UIWindowsListener> getUIWindowListeners() {
		return windowlisteners;
	}
	
}
