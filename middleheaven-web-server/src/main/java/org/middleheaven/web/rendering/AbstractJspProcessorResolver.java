package org.middleheaven.web.rendering;

import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.server.action.RequestResponseWebContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.web.Browser;
import org.middleheaven.ui.web.html.HtmlRenderKit;

/**
 * {@link RenderingProcessorResolver} that delegates to JSP pages.
 */
public abstract class AbstractJspProcessorResolver implements RenderingProcessorResolver{

	
	private HtmlRenderKit renderKit = new HtmlRenderKit();

	public AbstractJspProcessorResolver (){}
	
	public AbstractJspProcessorResolver (HtmlRenderKit renderKit){
		this.renderKit = renderKit;
	}
	
	/**
	 * 
	 * @param context the running {@link RequestResponseWebContext}.
	 */
	protected final void injectBrowserClient(RequestResponseWebContext context) {
		// set UIClient only for rendering
		Browser browser = new Browser(renderKit.getSceneNavigator());
		
		
		browser.setFamily(context.getAgent().getBrowserInfo().getBaseEngine());
		
		//TODO detect display size from cookies and javascript
		
		//browser.setBounds(0, 0, width, height);
		
		
		context.getAttributes().setAttribute(ContextScope.REQUEST,RenderKit.class.getName(), renderKit);
		context.getAttributes().setAttribute(ContextScope.REQUEST,UIClient.class.getName(), browser);
	}

}
