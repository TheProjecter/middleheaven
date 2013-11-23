package org.middleheaven.process.web.server.global;

import java.util.List;

import org.middleheaven.culture.Culture;
import org.middleheaven.process.web.server.HttpServerContext;

/**
 * Obtains the request culture directly from the information in the request.
 */
public final class RequestAgentHttpCultureResolver implements HttpCultureResolver {

	private Culture defaultCulture;

	public RequestAgentHttpCultureResolver(Culture culture){
		this.defaultCulture = culture;
	}
	
	public RequestAgentHttpCultureResolver(){
		this(Culture.defaultValue());
	}
	
	@Override
	public Culture resolveFrom(HttpServerContext context) {
		// TODO compare with supported cultures an use culture with maximum prioriy that is also supported
		final List<Culture> cultures = context.getCultures();
		return  cultures.isEmpty() ?  defaultCulture : cultures.get(0);
	}

}
