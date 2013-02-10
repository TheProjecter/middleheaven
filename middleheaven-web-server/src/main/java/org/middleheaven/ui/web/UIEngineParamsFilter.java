/**
 * 
 */
package org.middleheaven.ui.web;

import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.filters.HttpFilter;
import org.middleheaven.process.web.server.filters.HttpFilterChain;
import org.middleheaven.util.Splitter;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.collections.Enumerable;

/**
 * 
 */
public class UIEngineParamsFilter implements HttpFilter {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void doFilter(HttpServerContext context, HttpFilterChain chain) {


		String params = context.getAttributes().getAttribute("$ui_params", String.class);

		if (!StringUtils.isEmptyOrBlank(params)){
			Enumerable<String> parts = Splitter.on('&').split(params);

			for (String part : parts){
				String[] s = Splitter.on('=').split(part).intoArray(new String[0]);
				if (s.length == 1) {
					context.getAttributes().setAttribute(ContextScope.REQUEST, s[0], null);
				} else {
					context.getAttributes().setAttribute(ContextScope.REQUEST, s[0], s[1]);
				}


			}
		}
		
		chain.doChain(context);

	}

}
