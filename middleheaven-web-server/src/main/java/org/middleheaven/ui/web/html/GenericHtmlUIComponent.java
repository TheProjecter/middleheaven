package org.middleheaven.ui.web.html;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UIReadState;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.data.UIDataContainer;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;
import org.middleheaven.ui.rendering.RenderingContext;

public class GenericHtmlUIComponent implements UIComponent , UIContainer , UICommand, HTMLDocumentWritable, HtmlUIComponent {

	protected AbstractHtmlRender abstractHTMLRender;
	private String id;
	private String familly;
	private Class<? extends UIComponent> type;
	private UIComponent parent;
	private List<UIComponent> children = new LinkedList<UIComponent>();
	private Property<Boolean> visible = ValueProperty.writable("visible", true);
	private Property<Boolean> enabled = ValueProperty.writable("enabled", true);
	private Property<Boolean> required = ValueProperty.writable("required", false);
	private Property<String> name = ValueProperty.writable("name", String.class);
	private Property<Integer> maxLength = ValueProperty.writable("maxLength", Integer.class);
	private Property<Integer> minLength = ValueProperty.writable("minLength", Integer.class);
	private Property<Serializable> value = ValueProperty.writable("value", Serializable.class);
	private Property<UIReadState> readState = ValueProperty.writable("readState", UIReadState.class);
	private Property<LocalizableText> text = ValueProperty.writable("text", LocalizableText.class);
	private Property<ParsableFormatter> formatter = ValueProperty.writable("formatter", ParsableFormatter.class);
	private UILayout layout;
	private List<CommandListener> commandListeners = new ArrayList<CommandListener>();
	private UIDataContainer container;

	protected GenericHtmlUIComponent (){
		
	}
	
	public GenericHtmlUIComponent(UIComponent component, AbstractHtmlRender abstractHTMLRender) {
		this.abstractHTMLRender = abstractHTMLRender;
		this.type = component.getComponentType();
		this.id = component.getGID();
		this.familly = component.getFamily();
		this.visible.set(component.getVisibleProperty().get());
		this.enabled.set(component.getEnableProperty().get());
		//this.text.set(component.getTextProperty().get());
		//this.name.set(component.getNameProperty().get());
		
		if (component.isType(UICommand.class)){
			UICommand command = safeCast(component, UICommand.class).get();
			
			for (CommandListener listener : command.getCommandListeners()){
				commandListeners.add(listener);
			}
		}

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
		return UISize.pixels(0, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(0, 0);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDisplayableSize(UISize size) {
		throw new UnsupportedOperationException("Not supported");
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
	public Property<LocalizableText> getTextProperty() {
		return text;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<String> getNameProperty() {
		return name;
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

//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public Property<ParsableFormatter> getFormaterProperty() {
//		return formatter;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public Property<Boolean> getRequiredProperty() {
//		return required;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public Property<Integer> getMaxLengthProperty() {
//		return maxLength;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public Property<Integer> getMinLengthProperty() {
//		return minLength;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public Property<Serializable> getValueProperty() {
//		return value;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public Property<UIReadState> getReadStateProperty() {
//		return readState;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void setUIDataContainer(UIDataContainer container) {
//		this.container = container;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public UIDataItem getElementAt(int index) {
//		int i = 0;
//		for (UIDataItem item : container.getItems()){
//			if (i == index){
//				return item;
//			}
//			i++;
//		}
//		return null;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int getDataSize() {
//		return container.size();
//	}
//
//	int selectedIndex = -1;
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void clearSelection() {
//		selectedIndex = -1;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isSelectedIndex(int index) {
//		return selectedIndex == index;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public boolean isSelectionEmpty() {
//		return selectedIndex == -1;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int getMaxSelectionIndex() {
//		return selectedIndex;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int getMinSelectionIndex() {
//		return selectedIndex;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void setSelectionInterval(int start, int end) {
//		selectedIndex = start;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public void removeSelectionInterval(int start, int end) {
//		selectedIndex = -1;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public List<UIDataItem> getSelected() {
//		return selectedIndex < 0 ? Collections.<UIDataItem>emptyList() : Collections.singletonList(this.getElementAt(selectedIndex));
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	public int indexOf(UIDataItem anItem) {
//		int i = -1;
//		for (UIDataItem item : container.getItems()){
//			i++;
//			if (item.equals(anItem)){
//				return i;
//			}
//		}
//		return i;
//	}
	


	

}
