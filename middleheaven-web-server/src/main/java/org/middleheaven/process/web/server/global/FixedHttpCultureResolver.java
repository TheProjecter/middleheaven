package org.middleheaven.process.web.server.global;

import org.middleheaven.global.Culture;
import org.middleheaven.process.web.server.HttpServerContext;

public final class FixedHttpCultureResolver implements
		HttpCultureResolver {

	public static FixedHttpCultureResolver instanceFor(Culture culture){
		return new FixedHttpCultureResolver(culture);
	}

	private Culture culture;
	
	private FixedHttpCultureResolver(Culture culture){
		this.culture = culture;
	}
	
	@Override
	public Culture resolveFrom(HttpServerContext request) {
		return culture;
	}

}
