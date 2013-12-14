package org.middleheaven.process.web.server.action;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.middleheaven.collections.CollectionUtils;
import org.middleheaven.collections.enumerable.Enumerable;
import org.middleheaven.core.bootstrap.ServiceRegistry;
import org.middleheaven.core.wiring.WiringService;
import org.middleheaven.logging.Logger;
import org.middleheaven.process.AttributeContext;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.ScopedAttributesResolutionStrategy;
import org.middleheaven.process.web.HttpMethod;
import org.middleheaven.process.web.HttpRelativeUrl;
import org.middleheaven.process.web.HttpStatusCode;
import org.middleheaven.process.web.server.HttpServerContext;
import org.middleheaven.process.web.server.Outcome;
import org.middleheaven.process.web.server.WebContext;
import org.middleheaven.reflection.MethodFilters;
import org.middleheaven.reflection.ReflectedClass;
import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.reflection.ReflectedParameter;
import org.middleheaven.reflection.inspection.Introspector;
import org.middleheaven.util.Hash;
import org.middleheaven.util.coersion.TypeCoercing;
import org.middleheaven.util.function.Block;
import org.middleheaven.validation.ValidationException;
import org.middleheaven.web.annotations.Delete;
import org.middleheaven.web.annotations.Get;
import org.middleheaven.web.annotations.In;
import org.middleheaven.web.annotations.PathVariable;
import org.middleheaven.web.annotations.Post;
import org.middleheaven.web.annotations.ProcessRequest;
import org.middleheaven.web.annotations.Put;

/**
 * Delegates action execution to a Presenter class
 */
public class PresenterWebCommandMapping implements WebCommandMapping {

	private final Class<?> controllerClass;
	private Object controllerObject;
	private final List<ActionInterceptor> interceptors = new LinkedList<ActionInterceptor>();
	private final Map<String, Map<OutcomeStatus, OutcomeResolver>> outcomes = new HashMap<String, Map<OutcomeStatus, OutcomeResolver>>();
	private final Map<String , ReflectedMethod> actions = new HashMap<String,ReflectedMethod>();
	private final List<PathMatcher> patterns = new LinkedList<PathMatcher>();
	private final AttributeContextBeanLoader beanLoader = new AttributeContextBeanLoader();

	private EnumMap<HttpMethod, ReflectedMethod> serviceMethods = new EnumMap<HttpMethod, ReflectedMethod>(HttpMethod.class);

	private ReflectedMethod doService=null;

	/**
	 * 
	 * {@inheritDoc}
	 */
	public String toString(){
		return controllerClass.getName() + "->" + patterns.toString();
	}

	public int hashCode(){
		return Hash.hash(controllerClass.getName()).hash(patterns.size()).hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof PresenterWebCommandMapping) && equalsPresenterWebCommandMapping((PresenterWebCommandMapping)obj); 
	}


	private boolean equalsPresenterWebCommandMapping(PresenterWebCommandMapping other) {
		return this.controllerClass.equals(other.controllerClass) && this.patterns.size() == other.patterns.size() &&
				CollectionUtils.equalContents(this.patterns, other.patterns);
	}

	/**
	 * Constructor.
	 * @param presenterClass the presenter class.
	 */
	public PresenterWebCommandMapping(Class<?> presenterClass) {
		super();

		Map<OutcomeStatus, OutcomeResolver> actionOutcome = new HashMap<OutcomeStatus, OutcomeResolver>();
		this.outcomes.put(null,actionOutcome );

		for (OutcomeStatus status : BasicOutcomeStatus.values()){
			actionOutcome.put(status,new FixedOutcomeResolver(new Outcome(status,HttpStatusCode.NOT_IMPLEMENTED)));
		}

		this.controllerClass = presenterClass;

		// each method on the presenter that is not a getter or a setter is an action
		// only public not property methods
		Introspector.of(presenterClass).inspect().methods().notInheritFromObject().match(MethodFilters.publicInstanceNonProperty())
		.each(new Block<ReflectedMethod>(){

			@Override
			public void apply(ReflectedMethod m) {
				// a method can have multiple bindings 
				if (m.isAnnotationPresent(Post.class)){
					serviceMethods.put(HttpMethod.POST,m);
				} 
				if (m.isAnnotationPresent(Get.class)){
					serviceMethods.put(HttpMethod.GET,m);
				} 
				if (m.isAnnotationPresent(Delete.class)){
					serviceMethods.put(HttpMethod.DELETE,m);
				} 
				if (m.isAnnotationPresent(Put.class)){
					serviceMethods.put(HttpMethod.PUT,m);
				} 
				if (m.isAnnotationPresent(ProcessRequest.class)){
					doService = m;
				} else {
					actions.put(m.getName().toLowerCase(),m);
				}
			}

		});


	}

	public void addPathMatcher(String regex){
		patterns.add(PathMatcher.newInstance(regex));
	}

	@Override
	public double matches(HttpRelativeUrl url) {

		String dynamicUrl = url.toString();

		for (PathMatcher pattern : patterns){

			final double match = pattern.match(dynamicUrl);
			if (match > 0 ){
				return match;
			}

		}
		return 0d;
	}



	public void addOutcome(String action, OutcomeStatus status, OutcomeResolver outcomeResolver){
		Map<OutcomeStatus, OutcomeResolver> actionOutcome = this.outcomes.get(action);
		if (actionOutcome==null){
			actionOutcome = new HashMap<OutcomeStatus, OutcomeResolver>();
			this.outcomes.put(action,actionOutcome);
		}
		actionOutcome.put(status, outcomeResolver);
	}

	public Outcome resolveOutcome(String action, OutcomeStatus status, HttpServerContext context){

		Map<OutcomeStatus, OutcomeResolver> actionOutcome = this.outcomes.get(action);

		if (actionOutcome==null || status.equals(BasicOutcomeStatus.TERMINATE)){
			return new TerminalOutcome();
		} else {
			OutcomeResolver resolver = actionOutcome.get(status);
			if (resolver == null){
				return null;
			}
			return resolver.resolveOutcome(status, context);
		}
	}


	@Override
	public Outcome execute(HttpServerContext context) {

		if (controllerObject == null){
			controllerObject = ServiceRegistry.getService(WiringService.class).getInstance(controllerClass);
		}

		ListInterceptorChain chain = new ListInterceptorChain(this.interceptors){

			public Outcome doFinal(HttpServerContext context){
				return executeAction(context);
			}

		};

		chain.doChain(context);

		return chain.getOutcome();
	}

	protected final Outcome executeAction(HttpServerContext context){ 


		AttributeContext attributes = context.getAttributes();

		Outcome outcome;
		ReflectedMethod actionMethod = null;
		String action = null;

		try {
			// Determine action using different strategies

			// try the name match from url

			String actionNameFromURL = attributes.getAttribute("action", String.class);
			actionMethod = actionNameFromURL==null ? null : actions.get(actionNameFromURL);

			if (actionMethod==null){
				// try the name match if there are any parameters
				final ScopedAttributesResolutionStrategy parameters = context.getAttributes().getScopeAttributeContext(ContextScope.REQUEST_PARAMETERS);
				if (!parameters.isEmpty()){
					for ( Map.Entry<String, ReflectedMethod> entry : actions.entrySet()){
						String act = parameters.getAttribute(entry.getKey(), String.class);
						if (act!=null){
							actionMethod = entry.getValue();
							break;
						}
					}
				}
				if (actionMethod==null){
					// try the service match by type of request
					actionMethod = serviceMethods.get(context.getRequestMethod());
				}

				if (actionMethod==null && doService!=null){ 
					// try generic service
					actionMethod = doService; 
				} 

				if (actionMethod==null){
					if (context.getRequestMethod().equals(HttpMethod.GET)){
						return resolveOutcome(null,BasicOutcomeStatus.SUCCESS, context);
					} else {
						Logger.onBookFor(this.getClass()).warn("No handler found for action {0} or method {1} in presenter {2}. Request came from {3}" , actionNameFromURL, context.getRequestMethod(), controllerClass, context.getHttpChannel().getRemoteAddr());
						return new Outcome(BasicOutcomeStatus.ERROR, HttpStatusCode.NOT_FOUND);
					}
				}
			}
			action = actionMethod.getName().toLowerCase();

			Object[] args = inicilizeActionParameters(actionMethod, context);

			Object result = actionMethod.getReturnType().cast(actionMethod.invoke(controllerObject, args));

			if (result instanceof Outcome){
				return (Outcome)result;
			}else if (result instanceof String) {
				return new Outcome(BasicOutcomeStatus.SUCCESS, result.toString(), "text/html");
			} else if (!(result instanceof OutcomeStatus)){
				return resolveOutcome(action,BasicOutcomeStatus.SUCCESS,context);
			} else if (BasicOutcomeStatus.NOT_FOUND.equals(result)){
				return new Outcome(BasicOutcomeStatus.ERROR, HttpStatusCode.NOT_FOUND);
			}  else {
				return resolveOutcome(action,(OutcomeStatus)result, context);
			}

		} catch (ValidationException e){
			attributes.setAttribute(ContextScope.REQUEST, "validationResult", e.getResult());
			outcome =  resolveOutcome(action,BasicOutcomeStatus.INVALID,context );
		} catch (ActionHandlerNotFoundException e){
			Logger.onBookFor(this.getClass()).fatal(e,"Action not found");
			outcome =  resolveOutcome(action,BasicOutcomeStatus.ERROR, context);
		}catch (Exception e){
			Logger.onBookFor(this.getClass()).error(e,"Exception found handling request");
			attributes.setAttribute(ContextScope.REQUEST, "exception", e);
			outcome =  resolveOutcome(action,BasicOutcomeStatus.FAILURE, context);
		} 
		return outcome;
	}

	private Object[] inicilizeActionParameters(ReflectedMethod action,HttpServerContext context){
		Enumerable<ReflectedParameter> parameters = action.getParameters();
		Object[] args = new Object[parameters.size()];

		Map<String, String> variables = resolvePathVariablesValues(context.getRequestUrl().getContexlessPath() + context.getRequestUrl().getFilename(true));

		int i =0;
		for (ReflectedParameter parameter : parameters ){
			final ReflectedClass<?> type = parameter.getType();
			if (type.isAssignableFrom(WebContext.class)){
				args[i] = context;
			} else if (type.isAssignableFrom(AttributeContext.class)){
				args[i] = context.getAttributes();
			} else if (type.isAssignableFrom(HttpServletRequest.class)){
				if (context instanceof RequestResponseWebContext){
					args[i] = ((RequestResponseWebContext)context).getServletRequest();
				} else {
					throw new IllegalStateException("Is not possible to inject " + HttpServletRequest.class.getName() + " on current environment");
				}
			} else if (type.isAssignableFrom(HttpServletResponse.class)){
				if (context instanceof RequestResponseWebContext){
					args[i] = ((RequestResponseWebContext)context).getServletResponse();
				} else {
					throw new IllegalStateException("Is not possible to inject " + HttpServletResponse.class.getName() + " on current environment");
				}
			} else {
				final Enumerable<Annotation> annotations = parameter.getAnnotations();
				if (annotations.size() > 0){

					for (Annotation anot : annotations){
						if (anot instanceof PathVariable){
							String pathVariableName = ((PathVariable) anot).value();
							args[i] = readPathVariable(pathVariableName ,  type.getReflectedType(), variables);
							break;
						} else if (anot instanceof In){
							args[i] = context.getAttributes().getAttribute(((In) anot).value(), type.getReflectedType());
							break;
						}
					}
				}else if (!type.isPrimitive()){
					args[i]=beanLoader.loadBean(context, type.getReflectedType(), new Annotation[0]);
				}
			}
			i++;
		}

		return args;
	}

	private Map<String, String> resolvePathVariablesValues(String contexlessPath){
		for (PathMatcher p : this.patterns){
			if (p.match(contexlessPath) > 0){
				return p.parse(contexlessPath);
			}
		}
		return Collections.emptyMap();
	}
	/**
	 * @param pathVariableName
	 * @param class1
	 * @param variables
	 * @return
	 */
	private Object readPathVariable(String pathVariableName, Class<?> type, Map<String, String> variables) {
		return TypeCoercing.coerce(variables.get(pathVariableName), type);
	}


	void addInterceptor(ActionInterceptor interceptor) {
		this.interceptors.add(interceptor);
	}

}
