/**
 * 
 */
package org.middleheaven.ui.web.html;

import java.io.Writer;

import org.middleheaven.process.MapContext;
import org.middleheaven.process.web.HttpEntry;
import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.HttpStatusCode;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.action.BasicOutcomeStatus;
import org.middleheaven.process.web.server.action.TerminalOutcome;
import org.middleheaven.ui.Rendering;
import org.middleheaven.ui.UIActionHandlerLocator;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIEnvironment;
import org.middleheaven.ui.UIEnvironmentType;
import org.middleheaven.ui.UISearch;
import org.middleheaven.ui.UIService;
import org.middleheaven.ui.components.UIWindow;
import org.middleheaven.ui.rendering.RenderKit;
import org.middleheaven.ui.rendering.RenderingContext;
import org.middleheaven.ui.web.UIClientRenderingProcessor;
import org.middleheaven.util.StringUtils;

/**
 * 
 */
public class StandardHtmlClientRenderingProcessor extends UIClientRenderingProcessor {

	private UIClient client;
	private RenderKit renderKit;

	/**
	 * 
	 * Constructor.
	 * @param uiService the client to render
	 * @param env the render kit used
	 */
	public StandardHtmlClientRenderingProcessor (UIService uiService, UIEnvironment env){
		uiService.registerEnvironment(env, new HtmlRenderKit() , new MapContext());
		
	
		Rendering<UIClient> rendering =  uiService.getUIClientRendering(UIEnvironmentType.BROWSER);
		
		this.renderKit = rendering.getRenderKit();
		this.client = rendering.getComponent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Outcome doProcess(HttpServerContext context) throws HttpProcessException {
		try {
			String windowId = parseWindowName(context);
			
			UIComponent window = UISearch.absolute(client).search("#" + windowId).first();
					
			if (window == null){
				return new Outcome(BasicOutcomeStatus.NOT_FOUND , HttpStatusCode.NOT_FOUND);
			} else if (!window.isType(UIWindow.class)) {
				return new Outcome(BasicOutcomeStatus.NOT_FOUND , HttpStatusCode.NOT_FOUND);
			} else if (!window.isRendered()){
				return new Outcome(BasicOutcomeStatus.FAILURE , HttpStatusCode.SERVICE_UNAVAILABLE);
			}
			
			String action = context.getAttributes().getAttribute("$ui_action", String.class);
			
			final HttpEntry entry = context.getResponse().getEntry();
			if (!StringUtils.isEmptyOrBlank(action)){
				
				// the command id
				String gid = context.getAttributes().getAttribute("$ui_gid", String.class);
				
				UIComponent d = UISearch.absolute(client).search("#" + gid).first();
				
				if (d != null){
					UIActionHandlerLocator.getLocator(context.getAttributes()).handle(action).from(d);
					
				}
				
				entry.getContent().setContentType("application/json");
				
				Writer writer = entry.getContentWriter();
				
				writer.append("{")
				.append("\"nextUrl\":").append("\"ok_ok\"")
				.append("}");
				
				writer.close();
				
				return new TerminalOutcome();
				
			} else {
				final RenderingContext r = new RenderingContext(context.getAttributes(),  renderKit);

				HtmlDocument doc = HtmlDocument.newInstance(context.getContextPath(), context.getCulture());

				((HtmlUIComponent) window).writeTo(doc, r);

				entry.getContent().setContentType("text/html; charset=UTF-8");
				
				doc.writeToResponse(entry.getContentWriter());
				
				return new TerminalOutcome();
			}
			
		
		

	
			
		} catch (Exception e) {
			throw new HttpProcessException(e);
		} 







	}

	

	/**
	 * @param context
	 * @return
	 */
	private String parseWindowName(HttpServerContext context) {
		return context.getRequestUrl().getFilename(true);	
	}


}
