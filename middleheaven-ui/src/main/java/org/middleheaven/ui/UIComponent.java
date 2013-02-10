package org.middleheaven.ui;

import java.util.List;

import org.middleheaven.ui.components.UIInput;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.util.property.Property;


/**
 * Abstract base visual component.  
 *
 *Components have a composite raltion with each other. 
 *Compoennts can be added to other components. 
 *Each component is rendered by a {@link RenderKit}. the {@link RenderKit} determines the rendering by
 *the component's type and family. 
 */
public interface UIComponent extends  Displayable {
	
    /**
     * GID is a unique Graphic Identification. Each Component should have an unique id.
     * @return ID that uniquely identifies this component
     */
    public String getGID();
    
    /**
     * GID is a unique Graphic Identification. Each Component should have an unique id.
     * @param id the unique Graphic Identification.
     */
    public void setGID(String id);

    /**
     * 
     * @return <code>true</code> is this component is already rendered, i.e. if it is a 
     * proper component of a rendered hierarchy 
     */
    public boolean isRendered();
    

    /**
     * 
     * @return component rendering type
     */
    public <T extends UIComponent> Class<T> getComponentType();
    
    /**
     * @return component rendering family
     */
    public String getFamily();
    
   
    public void setFamily(String family);
    
    /**
     * @return this components parent component
     */
    public UIComponent getUIParent();
    

    public void setUIParent(UIComponent parent);
    
    /**
     * @return ummodifiable children components set as a <code>List</code>. Use <code>addComponent()</code> and <code>removeComponent()</code>
     * to edit the set.
     */
    public List<UIComponent> getChildrenComponents();
    
    /**
     * 
     * @return number of children components. Preferable to getChildrenComponents().size();
     */
    public int getChildrenCount();
    
    /**
     * The visible property.
     * @param visible <code>true</code> if the component is visible to the user, <code>false</code> otherwise.
     */
    public Property<Boolean> getVisibleProperty();
    
    /**
     * The enable property. Only {@link UIInput}
     * @param enabled <code>true</code> if the component can interact with the user, <code>false</code> otherwise.
     */
    public Property<Boolean> getEnableProperty();
    
    
    /**
     * @see Object#equals(Object)
     */
    public boolean equals(Object other);
    
    /**
     * @see Object#hashCode()
     */
    public int hashCode();

	

	/**
	 * Test if this component if of the given type.
	 * 
	 * @param type the component type to test.
	 * 
	 * @return <code>true</code> if this component is of the given type, <code>false</code> otherwise.
	 */
	public boolean isType(Class<? extends UIComponent> type);

	


    

}
