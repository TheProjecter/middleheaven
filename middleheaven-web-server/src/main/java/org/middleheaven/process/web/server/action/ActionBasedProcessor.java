package org.middleheaven.process.web.server.action;

import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.HttpRelativeUrl;
import org.middleheaven.process.web.HttpStatusCode;
import org.middleheaven.process.web.server.AbstractHttpProcessor;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;

public class ActionBasedProcessor extends AbstractHttpProcessor{

	private WebCommandMappingService Function;
	
	public ActionBasedProcessor (WebCommandMappingService Function){
		this.Function = Function;
	}

	@Override
	public Outcome doProcess(HttpServerContext context) throws HttpProcessException {

		try {

			// resolve mapped WebCommand from url

			WebCommandMapping webCommandMapping = Function.resolve(stripedRequestPath(context));
			
			Outcome outcome;
			
			if (webCommandMapping==null){
				// resource not found
				return new Outcome(BasicOutcomeStatus.ERROR, HttpStatusCode.NOT_FOUND);
			} 
			
			
			return webCommandMapping.execute(context);
			
		} catch ( ActionHandlerNotFoundException e ){
			return new Outcome(BasicOutcomeStatus.ERROR, HttpStatusCode.NOT_IMPLEMENTED);
		}

	}

	/**
	 * Removes the context part of the url returning a relative path.
	 * 
	 * @param context
	 * @return
	 */
	private HttpRelativeUrl stripedRequestPath(HttpServerContext context) {
		StringBuilder requestURL = new StringBuilder(context.getRequestUrl().toString());
		
		// remove context from the start and suffix from end
		String strip;
		final int start;
		if(context.getContextPath().isEmpty()){ 
			// running as root
			String PROTOCOL_SEPARATOR = "://";
			start = requestURL.indexOf("/", requestURL.indexOf(PROTOCOL_SEPARATOR) + PROTOCOL_SEPARATOR.length() + 1) + 1;
		
		} else {
			// running on context
			
			start = requestURL.indexOf(context.getContextPath()) + context.getContextPath().length()+1;
		}
		
	
		final int end = requestURL.lastIndexOf(".");
		if(end>0){
			strip = requestURL.substring( start, end);
		} else {
			strip = requestURL.substring(start);
		}
		
		
		String[] url = strip.split("\\."); 
		if ( url.length>1){
			context.getAttributes().setAttribute(ContextScope.REQUEST, "action", url[1]);
		}
		return new HttpRelativeUrl( url[0], context.getContextPath());
	}


}
