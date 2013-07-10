/*
 * Created on 11/08/2005
 *
 */
package org.middleheaven.ui.rendering;

import java.io.Serializable;

import org.middleheaven.ui.SceneNavigator;
import org.middleheaven.ui.UIComponent;


/**
 * A set of render used to render the final UI
 */
public abstract class RenderKit implements Serializable{

	private static final long serialVersionUID = -3529764505415405513L;

	public interface RenderProperties extends Serializable{
        
        public String getFamilly();
        public <T extends UIComponent> Class<T> getRenderType();
        
    }
    
    protected RenderTheme theme;

    public RenderKit(){
      
    }
    
    public RenderTheme getRenderTheme(){
        return theme;
    }
    
    public void setRenderTheme(RenderTheme theme){
        this.theme = theme;
        this.isThemeInstalled = false;
    } 
    
    private transient boolean isThemeInstalled= false;
    
    protected void installTheme(){
        if (theme!=null && !isThemeInstalled){
            this.theme.installTheme(this);
            this.isThemeInstalled = true;
        }
    }
    
    /**
     * Render the component by determining the render automatically.
     * The RenderKit will first try to find a matching {@link UIRender} by the component Id. If none is found will try to find it using the components type.
     * 
     */
    public abstract  <T extends UIComponent> T renderComponent(RenderingContext context,UIComponent parent, T component);
    
    /**
     * Render the component using the given {@link UIRender}.  
     * @param render the render to use
     * @param context the current {@link RenderingContext}
     * @param parent the already rendered parent component.
     * @param component the not rendered component.
     * @return
     */
    public abstract  <T extends UIComponent> T renderComponent(UIRender render, RenderingContext context,UIComponent parent, T component);
    
    /**
     * Find the appropriate  <code>Render</code> for the given component type
     * The search is made in this order:<BR>
     * 1) Find the component's familly. If its null or and empty string 
     * the default familly is used 
     * 2) Look in the renders familly for the choosen render
     * 
     * @param renderType
     * @return the appropriate  <code>Render</code> for the given component type or {@code null} is none is found.
     */
    public  abstract <T extends UIComponent>  UIRender getRender(Class<T> componentType, String familly);
    
    /**
     * Register an {@link UIRender} for a specific Component Type and family
     * @param render therender to register
     * @param componentType the component type to register it on.
     * @param familly the family of the component
     */
    public abstract <T extends UIComponent>  void addRender(UIRender render, Class<T> componentType, String familly);
    
    /**
     * Registers an {@link UIRender} for a specific component.
     * This {@code UIRender} will only be used for this component an takes precedence over any render defined for the component type.
     * 
     * @param render the render to register
     * @param componentID the id of the component.
     */
    public abstract void setRender (UIRender render, String componentID);

    /**
     * Retruns the registed {@link UIRender} for the component with the given id, or <code>null</code> if no UIRender was registered.
     * @param componentID the component ID.
     * @return the registed {@link UIRender} for the component with the given id, or <code>null</code> if no UIRender was registered.
     */
    public  abstract UIRender getRender(String componentID);
    
    
    /**
     * 
     * @return {@link UIUnitConverter} for the rendering tencology
     */
    public abstract UIUnitConverter getUnitConverted();

    /**
     * 
     * @return  {@link SceneNavigator} for the rendering tencology
     */
    public abstract SceneNavigator getSceneNavigator();
    
}
