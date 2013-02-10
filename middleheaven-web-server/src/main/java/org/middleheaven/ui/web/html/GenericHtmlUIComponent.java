package org.middleheaven.ui.web.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.global.text.TextLocalizable;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.util.property.Property;
import org.middleheaven.util.property.ValueProperty;

public class GenericHtmlUIComponent implements UIComponent , UIContainer , UICommand, HTMLDocumentWritable, HtmlUIComponent {

	protected AbstractHtmlRender abstractHTMLRender;
	private String id;
	private String familly;
	private Class<? extends UIComponent> type;
	private UIComponent parent;
	private List<UIComponent> children = new LinkedList<UIComponent>();
	private Property<Boolean> visible = ValueProperty.writable("visible", true);
	private Property<Boolean> enabled = ValueProperty.writable("enabled", true);
	private UILayout layout;
	private ArrayList<CommandListener> commandListeners = new ArrayList<CommandListener>();

	protected GenericHtmlUIComponent (){
		
	}
	
	public GenericHtmlUIComponent(UIComponent component, AbstractHtmlRender abstractHTMLRender) {
		this.abstractHTMLRender = abstractHTMLRender;
		this.type = component.getComponentType();
		this.id = component.getGID();
		this.familly = component.getFamily();
		this.visible.set(component.getVisibleProperty().get());
		this.enabled.set(component.getEnableProperty().get());
		
		if (component.isType(UICommand.class)){
			UICommand command = (UICommand)component;
			
			for (CommandListener listener : command.getCommandListeners()){
				commandListeners.add(listener);
			}
		}
		
		commandListeners.trimToSize();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeTo(HtmlDocument doc,RenderingContext context) throws IOException{
		this.abstractHTMLRender.write(doc, context, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString(){
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UISize getDisplayableSize() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIPosition getPosition() {
		throw new UnsupportedOperationException("Not implememented yet");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDisplayableSize(UISize size) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGID() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGID(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRendered() {
		return true;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) this.type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFamily() {
		return this.familly;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFamily(String familly) {
		this.familly = familly;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent getUIParent() {
		return this.parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIParent(UIComponent parent) {
	   this.parent = parent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount() {
		return children.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component) {
		children.add(component);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponent(UIComponent component) {
		children.remove(component);
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout component) {
		this.layout = component;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayout getUIContainerLayout() {
		return this.layout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstrain) {
		
		if (this.layout != null){
			this.layout.addComponent(component, layoutConstrain);
			this.addComponent(component);
		}
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
		return enabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<TextLocalizable> getTextProperty() {
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
		commandListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCommandListener(CommandListener listener) {
		commandListeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<CommandListener> getCommandListeners() {
		return commandListeners;
	}
	


	

}
