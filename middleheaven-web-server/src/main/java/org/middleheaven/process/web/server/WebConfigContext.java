package org.middleheaven.process.web.server;

import javax.servlet.ServletContext;

import org.middleheaven.culture.Culture;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ScopedAttributesResolutionStrategy;
import org.middleheaven.util.coersion.TypeCoercing;

public class WebConfigContext implements AttributeContext {

	ServletContext ctx;
	private Culture culture;
	
	public WebConfigContext(){
		
	}
	
	public WebConfigContext(ServletContext ctx) {
		this.ctx = ctx;
		String sCulture = ctx.getInitParameter("culture");
		if (sCulture !=null){
			this.culture = Culture.valueOf(sCulture.split("_"));
		} else {
			this.culture = Culture.defaultValue();
		}
		
	}

	
	@Override
	public <T> T getAttribute(ContextScope scope, String name, Class<T> type) {
		Object value;
		switch (scope){
		case CONFIGURATION:
			value = ctx.getInitParameter(name);
			break;
		case APPLICATION:
			value = ctx.getAttribute(name);
			break;
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}

		if ( value == null){
			return null;
		}
		return TypeCoercing.coerce(value, type);

	}

	@Override
	public void setAttribute(ContextScope scope, String name, Object value) {
		
		switch (scope){
		case APPLICATION:
			ctx.setAttribute(name,value);
			break;
		case CONFIGURATION:
			throw new IllegalArgumentException(scope + " is read-only");
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
	}

	@Override
	public <T> T getAttribute(String name, Class<T> type) {
		ContextScope[] scopes = new ContextScope[]{
			ContextScope.APPLICATION,
			ContextScope.CONFIGURATION};
		for (ContextScope scope : scopes){
			T obj = this.getAttribute(scope, name, type);
			if (obj!=null){
				return obj;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopedAttributesResolutionStrategy getScopeAttributeContext(ContextScope scope) {
		switch (scope){
		case CONFIGURATION:
			return new ServletInitConfigurationScopedAttributesResolutionStrategy(ctx);
		case APPLICATION:
			return new ServletApplicationScopedAttributesResolutionStrategy(ctx);
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttribute(ContextScope scope, String name) {
		switch (scope){
		case APPLICATION:
			ctx.removeAttribute(name);
		default:
			throw new IllegalArgumentException("Unavailable scope " + scope);
		}
	}



}
