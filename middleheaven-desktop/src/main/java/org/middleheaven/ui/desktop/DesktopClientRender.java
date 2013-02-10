package org.middleheaven.ui.desktop;

import org.middleheaven.process.ContextScope;
import org.middleheaven.process.progress.BoundProgress;
import org.middleheaven.process.progress.Progress;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.rendering.UIRender;

/**
 * 
 */
public class DesktopClientRender  extends UIRender {


	private static final long serialVersionUID = -171038403911015067L;

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isChildrenRenderer(RenderingContext context, UIComponent parent,UIComponent component) {
		return true;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	protected UIComponent build(RenderingContext context, UIComponent parent, UIComponent component) {
		
		Desktop dclient = new Desktop(context.getRenderKit().getSceneNavigator());
		
		// parent is null has the client is a root element
		RenderKit renderKit = context.getRenderKit();
		
		Progress progress = new BoundProgress(component.getChildrenCount());

		context.setAttribute(ContextScope.RENDERING, "progress", progress);

		UIComponent splash = dclient.resolveSplashWindow((UIClient)component,context);
		
		// show progress
		if (splash!=null){
			if (!splash.isRendered()){
				splash = renderKit.renderComponent(context, dclient, splash);
			}
			splash.setUIParent(dclient);
			renderKit.getSceneNavigator().show(splash);
			// TODO splash.gainFocus();
		}
		
		
		for (UIComponent comp : component.getChildrenComponents()){
			
			
			UIComponent renderedComponent = renderKit.renderComponent(context, dclient, comp);

			dclient.addComponent(renderedComponent);
			
			progress.increment();
		}
		
		
		renderKit.getSceneNavigator().dispose(splash);
	
		return dclient;
	}

}
