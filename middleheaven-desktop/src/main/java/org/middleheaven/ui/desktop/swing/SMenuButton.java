package org.middleheaven.ui.desktop.swing;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UILabel;
import org.middleheaven.ui.events.UIActionEvent;
import org.middleheaven.ui.property.BindedProperty;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;

public class SMenuButton extends JMenuItem implements UICommand {

	private static final long serialVersionUID = 1L;
	
	private String family;
	private String id;
	private UIComponent parent;

	private final Property<Boolean> visible = BindedProperty.bind("visible" , this);
	private final Property<Boolean> enable = BindedProperty.bind("enable" , this);
	private final Property<LocalizableText> text = ValueProperty.writable("text", LocalizableText.class);
	
	private final EventListenersSet<CommandListener> commandListeners = EventListenersSet.newSet(CommandListener.class);
	
	
	public SMenuButton(){
		super ();
		
		this.setAction(new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				commandListeners.broadcastEvent().onCommand(new UIActionEvent(getName(), SMenuButton.this));
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
	public List<UIComponent> getChildrenComponents() {
		return Collections.emptyList();
	}

	@Override
	public int getChildrenCount() {
		return 0;
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
		return (Class<T>) UILabel.class;
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
		this.id= id;
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
		return UIPosition.pixels(this.getX(),this.getY());
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
	public Property<LocalizableText> getTextProperty() {
		return text;
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
	public void addCommandListener(CommandListener commandListener) {
		this.commandListeners.addListener(commandListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCommandListener(CommandListener commandListener) {
		this.commandListeners.removeListener(commandListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<CommandListener> getCommandListeners() {
		return this.commandListeners;
	}


}
