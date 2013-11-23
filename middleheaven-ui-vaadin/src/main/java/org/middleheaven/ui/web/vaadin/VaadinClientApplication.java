/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import static org.middleheaven.util.SafeCastUtils.safeCast;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.middleheaven.culture.Culture;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.web.server.ServletWebContext;
import org.middleheaven.ui.SceneNavigator;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;

/**
 * 
 */
public class VaadinClientApplication extends Application implements UIClient{

	
	private String gid;
	private String family;
	private Map <String, VaadinUIComponent> components = new LinkedHashMap<String,VaadinUIComponent>();
	private ServletWebContext renderingContext;
	private SceneNavigator navigator;
	private UILayout layout;
	private Window bowserWindow;
	
	public VaadinClientApplication (ServletWebContext renderingContext, SceneNavigator navigator){
		this.renderingContext = renderingContext;
		this.navigator = navigator;
	}
	
    public void start(URL applicationUrl, Properties applicationProperties, ApplicationContext context) {
    	// reset the renderingContext
    	this.renderingContext = null;
    	
    	super.start(applicationUrl, applicationProperties, context);
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		//no-op
		
		
		final Locale locale = ((WebApplicationContext)getContext()).getBrowser().getLocale();
		setLocale(locale);
		
		bowserWindow = new Window();
		bowserWindow.setVisible(true);
		bowserWindow.setSizeFull();
		bowserWindow.setLocale(locale);
		
		VaadinUIComponent component = (VaadinUIComponent)this.resolveMainWindow(this, getServletWebContext().getAttributes());
		
		bowserWindow.addComponent(component.getComponent());
		
		this.setMainWindow(bowserWindow);
		
	}
	
	protected ServletWebContext getServletWebContext(){
		if (this.renderingContext != null){
			return this.renderingContext;
		}
		return (ServletWebContext) ((WebApplicationContext)this.getContext()).getHttpSession().getAttribute("mhRequestResponseWebContext");
	}
	
	public Locale getLocale(){
		return getServletWebContext().getCulture().toLocale();
	}
	
	public Culture getCulture(){
		return getServletWebContext().getCulture();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	public UIPosition getPosition() {
		return UIPosition.pixels(0,0);
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
	public void setDisplayableSize(UISize size) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getGID() {
		return this.gid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setGID(String id) {
		this.gid = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIClient.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFamily() {
		return this.family;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent getUIParent() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIParent(UIComponent parent) {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UIComponent> getChildrenComponents() {
		return new ArrayList<UIComponent>(this.components.values());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount() {
		return this.components.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component) {
		
		final VaadinUIComponent c = safeCast(component, VaadinUIComponent.class).get(); 
		
		components.put(c.getGID(), c);
		
		//bowserWindow.addComponent(c.getComponent());
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponent(UIComponent component) {
		components.remove(component.getGID());
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
	public void terminate() {
		//no-op
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent findContainedComponent(String componentID) {
		return components.get(componentID);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SceneNavigator getSceneNavigator() {
		return navigator;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getVisibleProperty() {
		return ValueProperty.readOnly("visible", true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getEnableProperty() {
		return ValueProperty.readOnly("enable", true);
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
		return layout;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component, UILayoutConstraint layoutConstrain) {
		layout.addComponent(component, layoutConstrain);
		this.addComponent(component);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSplashWindowUsed() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent resolveMainWindow(UIClient client, AttributeContext context) {
		return this.getChildrenComponents().get(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent resolveSplashWindow(UIClient client, AttributeContext context) {
		return null;
	}




}
