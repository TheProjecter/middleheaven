package org.middleheaven.ui.desktop.swing;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIPrespectiveListener;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.components.UIWindowsListener;
import org.middleheaven.ui.events.UIPrespectiveEvent;
import org.middleheaven.ui.events.UIWindowEvent;
import org.middleheaven.util.collections.DelegatingList;
import org.middleheaven.util.property.BindedProperty;
import org.middleheaven.util.property.Property;

public class SWindow extends JFrame implements UIWindow{

	private static final long serialVersionUID = -5653696867299909583L;
	
	private final EventListenersSet<UIPrespectiveListener> prespectiveListeners = EventListenersSet.newSet(UIPrespectiveListener.class);
	private final EventListenersSet<UIWindowsListener> windowListeners = EventListenersSet.newSet(UIWindowsListener.class);
	
	private final Property<Boolean> visible = BindedProperty.bind("visible", this);
	private final Property<Boolean> enable = BindedProperty.bind("enable", this);
	private final Property<TextLocalizable> title = STextProperty.bind(this);
	
	private UIComponent parent;
	private String id;
	private String family;

	public SWindow(){

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
//		this.addFocusListener(new FocusListener (){
//
//			@Override
//			public void focusGained(FocusEvent e) {
//				model.onFocusGained(new UIFocusEvent(SWindow.this));
//			}
//
//			@Override
//			public void focusLost(FocusEvent e) {
//				model.onFocusLost(new UIFocusEvent(SWindow.this));
//			}
//			
//		});
		
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent event) {
				//Invoked the first time a window is made visible.
				prespectiveListeners.broadcastEvent().onOpen(new UIPrespectiveEvent(SWindow.this));
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// Invoked when a window has been closed as the result of calling dispose on the window.
				prespectiveListeners.broadcastEvent().onClosed(new UIPrespectiveEvent(SWindow.this));
			}

			@Override
			public void windowClosing(WindowEvent event) {
				// Invoked when the user attempts to close the window from the window's system menu. 
				// If the program does not explicitly hide or dispose the 
				// window while processing this event, the window close operation will be cancelled. 
				prespectiveListeners.broadcastEvent().onClosing(new UIPrespectiveEvent(SWindow.this));
				if (!SWindow.this.isVisible()){
					SWindow.this.dispose();
				}
			}

			@Override
			public void windowDeiconified(WindowEvent event) {
				// Invoked when a window is changed from a minimized to a normal state. 
				prespectiveListeners.broadcastEvent().onDeiconified(new UIPrespectiveEvent(SWindow.this));
			}

			@Override
			public void windowIconified(WindowEvent event) {
				//Invoked when a window is changed from a normal to a minimized state. 
				prespectiveListeners.broadcastEvent().onIconified(new UIPrespectiveEvent(SWindow.this));
			}

			
			@Override
			public void windowActivated(WindowEvent event) {
				// Invoked when the Window is set to be the active Window. 
				// Only a Frame or a Dialog can be the active Window. 
				// The native windowing system may denote the active Window or its children with special decorations,
				// such as a highlighted title bar. 
				// The active Window is always either the focused Window, or the first Frame or Dialog that is an owner 
				// of the focused Window. 
				windowListeners.broadcastEvent().onAtivated(new UIWindowEvent(SWindow.this));
			}
			
			@Override
			public void windowDeactivated(WindowEvent event) {
				// Invoked when a Window is no longer the active Window. 
				// Only a Frame or a Dialog can be the active Window. 
				// The native windowing system may denote the active Window or its children with special decorations, 
				// such as a highlighted title bar. 
				// The active Window is always either the focused Window, or the first Frame or Dialog that is an 
				// owner of the focused Window.
				
				windowListeners.broadcastEvent().onDeativated(new UIWindowEvent(SWindow.this));
			}


		
			
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		if (component instanceof JMenuBar){
			this.setJMenuBar((JMenuBar)component);
		} else {
			this.getContentPane().add(safeCast(component, JComponent.class).get());
		}
	}

	@Override
	public void removeComponent(UIComponent component) {
		this.getContentPane().remove(safeCast(component, JComponent.class).get());
	}
	
	@Override
	public List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return (UIComponent)getContentPane().getComponent(index);
			}

			@Override
			public int size() {
				return getComponentCount();
			}
			
		};
	}

	@Override
	public int getChildrenCount() {
		return getComponentCount();
	}

	@Override
	public String getFamily() {
		return family;
	}

	@Override
	public String getGID() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIWindow.class;
	}

	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean isRendered() {
		return true;
	}

	@Override
	public void setFamily(String family) {
		this.family = family;
	}

	@Override
	public void setGID(String id) {
		this.id = id;
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public void setDisplayableSize(UISize size) {
		UISize pixelSize =  SwingUnitConverter.getInstance().toPixels(size, this.getUIParent());

		this.setBounds(
				this.getX(), 
				this.getY(), 
				(int)pixelSize.getWidth().getValue(),
				(int)pixelSize.getHeight().getValue()
		);
	}

	
	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(this.getX(),this.getY());
	}


	@Override
	public UISize getDisplayableSize() {
		return UISize.pixels(this.getWidth(), this.getHeight());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout component) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayout getUIContainerLayout() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component,
			UILayoutConstraint layoutConstrain) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPrespectiveListener(UIPrespectiveListener listener) {
		prespectiveListeners.addListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePrespectiveListener(UIPrespectiveListener listener) {
		prespectiveListeners.removeListener(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addUIWindowListener(UIWindowsListener listener) {
		windowListeners.addListener(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<UIPrespectiveListener> getPrecpectiveListeners() {
		return prespectiveListeners;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeUIWindowListener(UIWindowsListener listener) {
		windowListeners.removeListener(listener);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<UIWindowsListener> getUIWindowListeners(){
		return windowListeners;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getVisibleProperty() {
		return visible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getEnableProperty() {
		return enable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<TextLocalizable> getTitleProperty() {
		return title;
	}
	
}
