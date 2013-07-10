package org.middleheaven.process.web.server.action;

import java.lang.annotation.Annotation;
import java.util.Date;

import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.global.LocalizationService;
import org.middleheaven.global.text.TimepointFormatter;
import org.middleheaven.process.Attribute;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.quantity.time.CalendarDateTime;
import org.middleheaven.util.StringUtils;
import org.middleheaven.util.coersion.StringCalendarDateTimeCoersor;
import org.middleheaven.util.coersion.StringDateCoersor;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.web.annotations.In;

public class AttributeContextBeanLoader {

	public <T> T loadBean(HttpServerContext context, Class<T> type ){
		return loadBean(context, type, new Annotation[0]);
	}

	public <T> T loadBean(HttpServerContext context, Class<T> type ,Annotation[] annotations) {
		try{
			// set up timestamp formatters and converter
			LocalizationService i18nService = ServiceRegistry.getService(LocalizationService.class);

			TimepointFormatter formatter = i18nService.getCultureModel(context.getCulture()).getTimestampFormatter();

			formatter.setPattern(TimepointFormatter.Format.DATE_ONLY);

			TypeCoercing.addCoersor(String.class, Date.class, new StringDateCoersor(formatter));
			TypeCoercing.addCoersor(String.class, CalendarDateTime.class, new StringCalendarDateTimeCoersor(formatter));


			String name = "";
			ContextScope inScope =null;
			In in = getAnnotation(annotations,In.class);

			final boolean isPrimitive = isPrimitive(type);
			
			if (isPrimitive && in == null){
				throw new IllegalArgumentException("Annotate parameter with @In for instances of " + type);
			}

			if (in!=null){
				name = in.value();
				inScope = in.scope();
				if(name.isEmpty()){
					throw new IllegalArgumentException("Cannot inject an unamed parameter");
				}
			} 

			AttributeContext attributes = context.getAttributes();
			
			if (isPrimitive){
				Object value = attributes.getAttribute(inScope, name, type);
				//outject
				attributes.setAttribute(ContextScope.REQUEST, name.toLowerCase(), value);
				return type.cast(value);
			} else {
				T object = null;

				// search in the contexts in reverse order to find an object of class type
				ContextScope[] scopes = new ContextScope[]{
						ContextScope.REQUEST,
						ContextScope.SESSION,
						ContextScope.APPLICATION
				};


				OUTTER_LABEL: 
				for (ContextScope scope : scopes){
					for (Attribute attribute : attributes.getScopeAttributeContext(scope)){
						Object candidate = attribute.getValue();
						if (candidate != null && type.isAssignableFrom(candidate.getClass())){
							object = type.cast(candidate);
							break OUTTER_LABEL;
						}
					}
				}

				if (object==null){
					if(name.isEmpty()){
						name = StringUtils.firstLetterToLower(type.getSimpleName());
					}
					// try to load from parameters
					object =  new ContextAssembler(
									ServiceRegistry.getService(WiringService.class), 
									attributes,
									ContextScope.REQUEST_PARAMETERS,
									name
							).assemble(type);
					
				}
				
				//outject
				attributes.setAttribute(ContextScope.REQUEST, name.toLowerCase(), object);
				return object;

			}
		} finally {
			TypeCoercing.removeCoersor(String.class, Date.class);
			TypeCoercing.removeCoersor(String.class, CalendarDateTime.class);

		}



	}

	private <A extends Annotation> A getAnnotation(Annotation[] annotations ,Class<A> annotationClass){
		if (annotations !=null){
			for (Annotation a : annotations){
				if(annotationClass.isAssignableFrom(a.getClass())){
					return annotationClass.cast(a);
				}
			}
		}
		return null;
	}


	private boolean isPrimitive(Class<?> type){
		return type.isPrimitive() || 
		String.class.isAssignableFrom(type) || 
		Date.class.isAssignableFrom(type)|| 
		Number.class.isAssignableFrom(type);
	}
}
