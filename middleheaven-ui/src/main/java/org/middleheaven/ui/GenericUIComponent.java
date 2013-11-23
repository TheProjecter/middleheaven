package org.middleheaven.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.middleheaven.core.reflection.inspection.Introspector;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.global.text.ParsableFormatter;
import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UIField;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UILayoutManager;
import org.middleheaven.ui.data.UIDataContainer;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;
import org.middleheaven.ui.rendering.RenderKit;

/**
 * Generic, {@link RenderKit} agnostic representation of a {@link UIComponent}.
 * 
 * @param <T> the {@link UIComponent} represented by the object.
 */
public class GenericUIComponent<T extends UIComponent> implements UIContainer , UIField , UICommand, UILayout {

	static private int nextID=0;
	
	private List<UIComponent> children = new CopyOnWriteArrayList<UIComponent>();
	private UILayout layout = null;
	private String id;
	private String familly; 
	private Class<T> renderType;
	private UIComponent parent;

	private UIDimension x = UIDimension.pixels(0);
	private UIDimension y = UIDimension.pixels(0);

	private UIDimension height;
	private UIDimension width;

	private Property<Boolean> visible = ValueProperty.writable("visible", true);
	private Property<Boolean> enable  = ValueProperty.writable("enable", true);
	private Property<LocalizableText> title  = ValueProperty.writable("title", LocalizableText.class);
	private Property<LocalizableText> text  = ValueProperty.writable("text", LocalizableText.class);
	private Property<UIReadState> readState  = ValueProperty.writable("readState", UIReadState.class);
	private Property<String> name = ValueProperty.writable("name", String.class);
	private Property<ParsableFormatter> formater  = ValueProperty.writable("formater", ParsableFormatter.class);
	private Property<Boolean> required  = ValueProperty.writable("required", false);
	private Property<Integer> maxLength  = ValueProperty.writable("maxLength",Integer.MAX_VALUE );
	private Property<Integer> minLength  = ValueProperty.writable("minLength", 0);
	private Property<Serializable> value  = ValueProperty.writable("value", Serializable.class);
	
	private UIDataContainer dataContainer;
	
	private List<CommandListener> commandListeners = new ArrayList<CommandListener>();

	private UILayoutManager layoutManager;

	/**
	 * 
	 * @param uiClassInterface
	 * @param familly
	 * @return
	 */
	public static <T extends UIComponent>  T getInstance(Class<T> uiClassInterface, String familly){
		
		final GenericUIComponent<T> object = new GenericUIComponent<T>( uiClassInterface, familly);
		final GenericUIComponentProxyHandler proxyHandler = new GenericUIComponentProxyHandler(object,uiClassInterface);
		return Introspector.of(object).newProxyInstance(proxyHandler,uiClassInterface);
	}
	
	/**
	 * 
	 * Constructor.
	 * @param renderType
	 * @param familly
	 */
	protected GenericUIComponent(Class<T> renderType, String familly){
		this.id = Integer.toHexString(nextID++);
		this.renderType = renderType;
		this.familly = familly;
		
		if (renderType.equals(UIContainer.class)){
			layout = getInstance(UILayout.class,"border");
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}
	
	
	// Special Methods for genericUIComponent 
	
	public  GenericUIComponent addComponent(Class<T> renderType, String familly){
		return new GenericUIComponent( renderType,  familly);
	}
	
	public boolean equals(Object other){
		return other instanceof UIComponent && this.id.equals(((UIComponent)other).getGID());
	}
	
	
	public int hashCode(){
		return this.id.hashCode();
	}
	
	public String toString(){
		return this.getClass().getSimpleName() + "." + this.renderType + "." +  id;
	}
	
	@Override
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstraint) {
		children.add(component); 
		if (this.layout != null){
			layout.addComponent(component, layoutConstraint);
		}
		if (this.layoutManager != null){
			layoutManager.addComponent(component, layoutConstraint);
		}
	}
	
	@Override
	public void addComponent(UIComponent component) {
		children.add(component);
	}

	@Override
	public void removeComponent(UIComponent component) {
		children.remove(component);
	}

	@Override
	public UILayout getUIContainerLayout() {
		return layout;
	}

	@Override
	public void setUIContainerLayout(UILayout component) {
		this.layout = component;
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.unmodifiableList(this.children);
	}

	@Override
	public int getChildrenCount() {
		return children.size();
	}

	@Override
	public String getFamily() {
		return familly;
	}

	@Override
	public String getGID() {
		return id;
	}

	@Override
	public Class<T> getComponentType() {
		return renderType;
	}


	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean isRendered() {
		return false;
	}

	@Override
	public void setFamily(String familly) {
		this.familly = familly;
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
		return UISize.valueOf(this.width,this.height);
	}

	@Override
	public UIPosition getPosition() {
		return UIPosition.valueOf(x, y);
	}

	@Override
	public void setDisplayableSize(UISize size) {
		this.width = size.getWidth();
		this.height = size.getHeight();
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

	public Property<LocalizableText> getTitleProperty() {
		return title;
	}
	
	public Property<LocalizableText> getTextProperty() {
		return text;
	}

	public Property<UIReadState> getReadStateProperty() {
		return readState;
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
	public void setUIDataContainer(UIDataContainer container) {
		dataContainer = container;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<ParsableFormatter> getFormaterProperty() {
		return formater;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getRequiredProperty() {
		return required;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Integer> getMaxLengthProperty() {
		return maxLength;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Integer> getMinLengthProperty() {
		return minLength;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Serializable> getValueProperty() {
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCommandListener(CommandListener listener) {
		this.commandListeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeCommandListener(CommandListener listener) {
		this.commandListeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<CommandListener> getCommandListeners() {
		return commandListeners;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayoutManager getLayoutManager() {
		return layoutManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLayoutManager(UILayoutManager layoutManager) {
		this.layoutManager = layoutManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIDataContainer getUIDataContainer() {
		return dataContainer;
	}
}
