
package org.middleheaven.ui.rendering;

import java.util.HashMap;
import java.util.Map;

import org.middleheaven.logging.Logger;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UILayoutManager;

/**
 * Base implementation for {@link RenderKit} classes.
 */
public abstract class AbstractRenderKit extends RenderKit {

	private static final long serialVersionUID = -1531256851197198897L;

	private final Map<RenderProperties, UIRender> typeRenders  = new HashMap<RenderProperties, UIRender>();   
	private final Map<String, UIRender> idRenders = new HashMap<String, UIRender>(); 
	
	/**
	 * 
	 * Constructor.
	 */
	public AbstractRenderKit(){

	}

	/**
	 * Determines if a given component is the rendered form of the component.
	 * @param component the component to test
	 * @return <code>true</code> if the component is a rendered form, <code>false</code> otherwise.
	 */
	protected boolean isRendered(UIComponent component) {
		return component.isRendered();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRender(UIRender render, String componentID) {
		idRenders.put(componentID, render);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public UIRender getRender(String componentID) {
		return idRenders.get(componentID);
	}

	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public UIComponent renderComponent(RenderingContext context,UIComponent parent, UIComponent component) {
		if (component==null){
			throw new IllegalArgumentException("Cannot render null");
		}
		
		// find by id 
		
		UIRender render = this.getRender(component.getGID());
		
		if (render == null){
			// if not found, find by type
			render = this.getRender(component.getComponentType(), component.getFamily());
		}
		
		if (render == null){
			throw new IllegalStateException("No Render registed for component (type=" + component.getComponentType().getSimpleName() +  ",id=" + component.getGID() + ", familly=" + component.getFamily());
		}
		
		return renderComponent(render,context,parent, component);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public  UIComponent renderComponent(UIRender render,RenderingContext context,UIComponent parent, UIComponent component) {
		if (component==null){
			throw new IllegalArgumentException("Cannot render null component");
		}
		if (render ==null){
			throw new IllegalStateException("Render not found for component " + component.getComponentType() + ":" + component.getFamily());
		}
		if (!UIClient.class.equals(component.getComponentType())){
			if (parent==null || !isRendered(parent)){
				throw new IllegalArgumentException("Parent component is not rendered. Parent is " + parent);
			}
		}

		this.installTheme();
		return build(render,context,parent,component);
	}

	private  UIComponent build(UIRender render , RenderingContext context, UIComponent parent, UIComponent component){

		// render component
		UIComponent renderedComponent = render.render(context, parent, component);
		
		// copy IDs
		renderedComponent.setGID(component.getGID());
		// copy family
		renderedComponent.setFamily(component.getFamily());
		
		if (parent != null && !(parent == renderedComponent.getUIParent() || parent.equals(renderedComponent.getUIParent()))){
			throw new IllegalStateException("The rendered should set the parent component");
		}

		// TODO copy state
		
		// if the component renders its children components then there is nothing else to do
		// otherwise render the children components and add them to it.
		if (component.isType(UIContainer.class)&& renderedComponent.isType(UIContainer.class)) {
			if (!render.isChildrenRenderer(  context,  parent,  component)){
				// does it need a container
				if (((UIContainer)renderedComponent).getUIContainerLayout() != null 
						&& !((UIContainer)renderedComponent).getUIContainerLayout().isRendered()){
					// render layout
					UILayout layout = (UILayout)this.renderComponent(context, component , ((UIContainer)component).getUIContainerLayout());
					((UIContainer)renderedComponent).setUIContainerLayout(layout);
				}

				for (UIComponent child : component.getChildrenComponents()){
					UIComponent rchild = renderComponent(context,renderedComponent,child);

					((UIContainer)renderedComponent).addComponent(rchild);
				}

			} 
		}
	

		if (this.theme!=null){
			this.theme.applyTheme(renderedComponent);
		}
		return renderedComponent;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public <T extends UIComponent> UIRender getRender(Class<T> componentType, String familly) {

		RenderPropertiesKey key = new RenderPropertiesKey(familly, componentType);

		UIRender render = typeRenders.get(key);

		if (render ==null){
			Logger.onBookFor(this.getClass()).warn("No render has found for familly {0} and type {1}. Using default familly", familly, componentType);
			// search default render
			key = new RenderPropertiesKey("", componentType);

			render = typeRenders.get(key);

		}
		return render;

	}

	/**
	 * Adds a {@link UIRender} render to this kit.
	 * The {@link UIRender} is registered to a given component type.
	 *  
	 * @param render the render to register.
	 * @param componentType the component type.
	 */
	public <T extends UIComponent>  void addRender(UIRender render, Class<T> componentType) {
		addRender(render,componentType,"");
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public final <T extends UIComponent>  void addRender(UIRender render,  Class<T> componentType, String familly) {

		RenderPropertiesKey key = new RenderPropertiesKey(familly, componentType);

		typeRenders.put(key,render);

	}

	protected final class RenderPropertiesKey implements RenderProperties{

		private static final long serialVersionUID = 1L;

		final String familly; 
		final Class<?> type;

		public RenderPropertiesKey(String familly , Class<?>  type){
			if(type ==null){
				throw new IllegalArgumentException("Type cannot be null");
			}
			this.familly = familly==null?"":familly.trim();
			this.type = type;
		}

		public final String getFamilly() {
			return familly;
		}


		public final <T extends UIComponent> Class<T> getRenderType() {
			return (Class<T>) type;
		}

		public final boolean equals(Object other){
			return other instanceof RenderPropertiesKey && equalsOther((RenderPropertiesKey)other);
		}

		private final boolean equalsOther(RenderPropertiesKey other){
			return this.type.getName().equals(other.type.getName()) && this.familly.equals(other.familly);
		}

		public final int hashCode(){
			return type.hashCode() ^ this.familly.hashCode();
		}
	}

}
