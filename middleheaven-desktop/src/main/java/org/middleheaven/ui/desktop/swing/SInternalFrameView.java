package org.middleheaven.ui.desktop.swing;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.middleheaven.collections.DelegatingList;
import org.middleheaven.events.EventListenersSet;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIPrespectiveListener;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.events.UIPrespectiveEvent;
import org.middleheaven.ui.property.BindedProperty;
import org.middleheaven.ui.property.Property;

public class SInternalFrameView extends JInternalFrame implements UIView{

	private static final long serialVersionUID = -7574540326870316945L;
	
	private String family;
	private String id;
	private UIComponent parent;

	private final EventListenersSet<UIPrespectiveListener> prespectiveListeners = EventListenersSet.newSet(UIPrespectiveListener.class);
	
	private Property<LocalizableText> title = STextProperty.bind(this);
	private Property<Boolean> visible = BindedProperty.bind("visible", this);
	private Property<Boolean> enable  = BindedProperty.bind("enable", this);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	public SInternalFrameView(){
		
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		
//		this.addFocusListener(new FocusListener(){
//
//			@Override
//			public void focusGained(FocusEvent e) {
//				model.onFocusGained(new UIFocusEvent(SInternalFrameView.this));
//			}
//
//			@Override
//			public void focusLost(FocusEvent e) {
//				model.onFocusLost(new UIFocusEvent(SInternalFrameView.this));
//			}
//			
//		});
		
		this.addInternalFrameListener(new InternalFrameListener(){

			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				//model.onFocusGained(new UIFocusEvent(SInternalFrameView.this));
			}
			
			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				//model.onFocusLost(new UIFocusEvent(SInternalFrameView.this));
			}

			@Override
			public void internalFrameOpened(InternalFrameEvent e) {
				prespectiveListeners.broadcastEvent().onOpen(new UIPrespectiveEvent(SInternalFrameView.this));
			}
			
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				prespectiveListeners.broadcastEvent().onClosed(new UIPrespectiveEvent(SInternalFrameView.this));
			}

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				prespectiveListeners.broadcastEvent().onClosing(new UIPrespectiveEvent(SInternalFrameView.this));
			}

			@Override
			public void internalFrameDeiconified(InternalFrameEvent e) {
				prespectiveListeners.broadcastEvent().onDeiconified(new UIPrespectiveEvent(SInternalFrameView.this));
			}

			@Override
			public void internalFrameIconified(InternalFrameEvent e) {
				prespectiveListeners.broadcastEvent().onIconified(new UIPrespectiveEvent(SInternalFrameView.this));
			}


		});
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
				return getContentPane().getComponentCount();
			}
			
		};
	}

	@Override
	public int getChildrenCount() {
		return this.getContentPane().getComponentCount();
	}

	@Override
	public String getFamily() {
		return family;
	}

	@Override
	public String getGID() {
		return id;
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIView.class;
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
	public UISize getDisplayableSize() {
		return UISize.pixels(this.getWidth(), this.getHeight());
	}

	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(this.getX(), this.getY());
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
	public Property<LocalizableText> getTitleProperty() {
		return title;
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
	public Iterable<UIPrespectiveListener> getPrecpectiveListeners() {
		return prespectiveListeners;
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
}
