/**
 * 
 */
package org.middleheaven.ui.web.vaadin;

import java.util.Collections;
import java.util.List;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.global.Culture;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIDimension;
import org.middleheaven.ui.UIDimensionUnit;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UISearch;
import org.middleheaven.ui.UISize;
import org.middleheaven.util.property.BindedProperty;
import org.middleheaven.util.property.Property;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Component;

/**
 * 
 */
public abstract class VaadinUIComponent implements UIComponent {
	
	private Component component;
	private String gid;
	private String family;
	protected UIComponent parent;
	private Class<? extends UIComponent> type;

	private final Property<Boolean> visible;
	private final Property<Boolean> enabled;
			
	private VaadinClientApplication app;

	/**
	 * 
	 * Constructor.
	 * @param component
	 * @param type
	 */
	public VaadinUIComponent(Component component, Class<? extends UIComponent> type){
		this.component = component;
		this.type = type;
		
		 visible = BindedProperty.bind("visible", component);
		 enabled = BindedProperty.bind("enabled", component);
		 
		 copyModel();
	}
	
	/**
	 * 
	 */
	protected abstract void copyModel();
	
	
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

	
	private VaadinClientApplication getApplication(){
		if (app == null){
			app = UISearch.absolute(this).self().first(VaadinClientApplication.class);
		}
		return app;
	}
	
	public Culture getCulture(){
		return  getApplication().getCulture();
	}
	
	/**
	 * @param text
	 * @param context
	 * @return
	 */
	protected final String localize(LocalizableText text) {
		if (text.isLocalized()){
			return text.toString();
		}
		return ServiceRegistry.getService(LocalizationService.class).getMessage(text,getCulture());
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public int hashCode(){
		return this.gid.hashCode();
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean equals(Object other){
		return  this == other || ((other instanceof VaadinUIComponent) && this.gid.equals(((VaadinUIComponent)other).gid));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) type;
	}
	
	/**
	 * Obtains {@link Component}.
	 * @return the component
	 */
	protected Component getComponent() {
		return component;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(0, 0);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final UISize getDisplayableSize() {
		return  UISize.valueOf(
				asDimension(this.component.getWidth() , this.component.getWidthUnits()), 
				asDimension(this.component.getHeight() , this.component.getHeightUnits())
				);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void setDisplayableSize(UISize size) {
		
		this.component.setWidth(size.getWidth().getValue(), asSizeableUnit(size.getWidth()));
		this.component.setHeight(size.getHeight().getValue(), asSizeableUnit(size.getHeight()));

	}
	
	
	private int asSizeableUnit(UIDimension dimension){
		switch (dimension.unit){
		case CM:
			return Sizeable.UNITS_CM;
		case EM: 
			return Sizeable.UNITS_EM;
		case EX:
			return Sizeable.UNITS_EX;
		case INCH:
			return Sizeable.UNITS_INCH;
		case MM:
			return Sizeable.UNITS_MM;
		case PERCENTAGE:
			return Sizeable.UNITS_PERCENTAGE;
		case PICAS: 
			return Sizeable.UNITS_PICAS;
		case POINTS:
			return Sizeable.UNITS_POINTS;
		case PIXELS:
		default:
			return Sizeable.UNITS_PIXELS;
		}
	}
	
	private UIDimension asDimension(float value, int unit){
		switch (unit){
		case Sizeable.UNITS_CM:
			return UIDimension.valueOf(value, UIDimensionUnit.CM);
		case Sizeable.UNITS_EM:
			return UIDimension.valueOf(value, UIDimensionUnit.EM);
		case Sizeable.UNITS_EX:
			return UIDimension.valueOf(value, UIDimensionUnit.EX);
		case Sizeable.UNITS_INCH:
			return UIDimension.valueOf(value, UIDimensionUnit.INCH);
		case Sizeable.UNITS_MM:
			return UIDimension.valueOf(value, UIDimensionUnit.MM);
		case Sizeable.UNITS_PERCENTAGE:
			return UIDimension.valueOf(value, UIDimensionUnit.PERCENTAGE);
		case Sizeable.UNITS_PICAS:
			return UIDimension.valueOf(value, UIDimensionUnit.PICAS);
		case Sizeable.UNITS_POINTS:
			return UIDimension.valueOf(value, UIDimensionUnit.POINTS);
		case Sizeable.UNITS_PIXELS:
		default:
			return UIDimension.valueOf(value, UIDimensionUnit.PIXELS);
		}
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
		this.component.setDebugId(id);
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
	public String getFamily() {
		return family;
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
	  return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildrenCount() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}

}
