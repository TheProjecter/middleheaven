package org.middleheaven.process.web.server.action;

import org.middleheaven.process.web.HttpStatusCode;
import org.middleheaven.process.web.server.Outcome;


public final class PresenterCommandMappingBuilder {

	
	public static PresenterCommandMappingBuilder map(Class<?> presenterClass, WebCommandMappingService webCommandMappingService){
		return new PresenterCommandMappingBuilder(presenterClass, webCommandMappingService);
	}
	
	
	private PresenterWebCommandMapping mapping;
	private WebCommandMappingService webCommandMappingService;

	private PresenterCommandMappingBuilder (Class<?> presenterClass, WebCommandMappingService webCommandMappingService){
		this.mapping = new PresenterWebCommandMapping(presenterClass);
		this.webCommandMappingService = webCommandMappingService;
	}
	
	public URLMappingBuilder to(String url){
		return new MyURLMappingBuilder(url);
	}
	
	public ActionMappingBuilder action(String name){
		return new MyActionMappingBuilder(name);
	}
	
	public WebCommandMapping getMapping(){
		return mapping;
	}

	public PresenterWebCommandMapping build(){
		return this.mapping;
	}

	
	private class MyURLMappingBuilder implements URLMappingBuilder {

		String url;
		public MyURLMappingBuilder(String urlPattern) {
			this.url = urlPattern;
			mapping.addPathMatcher(urlPattern);
		}

		/**
		 * 
		 * {@inheritDoc}
		 */
		@Override
		public MyURLMappingBuilder through(ActionInterceptor interceptor) {
			mapping.addInterceptor(interceptor);
			return this;
		}

		@Override
		public ActionMappingBuilder withAction(String actionName) {
			return new MyActionMappingBuilder(actionName);
		}

		@Override
		public ActionMappingBuilder withNoAction() {
			return new MyActionMappingBuilder(null);
		}

		@Override
		public URLMappingBuilder asIndex() {
			PresenterCommandMappingBuilder.this.to("")
			.withNoAction().onSuccess().redirectTo(url, RedirectOptions.permanent())
			.withAction("read").onSuccess().redirectTo(url, RedirectOptions.permanent());
			
			return this;
		}
		
	
	}
	

	private class MyActionMappingBuilder implements ActionMappingBuilder{
		
		private String action;
		
		public MyActionMappingBuilder (String action){
			this.action = action !=null ? action.toLowerCase() : null;
		}
		
		public OutcomeBuilder onSuccess (){
			return on(BasicOutcomeStatus.SUCCESS);
		}
		
		public OutcomeBuilder onFailure (){
			return on(BasicOutcomeStatus.FAILURE);
		}
		
		public OutcomeBuilder onInvalid (){
			return on(BasicOutcomeStatus.INVALID);
		}
		
		public OutcomeBuilder onError (){
			return on(BasicOutcomeStatus.ERROR);
		}
		public OutcomeBuilder on (OutcomeStatus status){
			return new MyOutcomeBuilder(this,status);
		}

		@Override
		public String getActionMame() {
			return action;
		}

		@Override
		public ActionMappingBuilder withAction(String actionName) {
			return new MyActionMappingBuilder(actionName);
		}
	}

	private class MyOutcomeBuilder implements OutcomeBuilder {
		private OutcomeStatus status;
		private ActionMappingBuilder actionBuilder;
		private String contentType= "text/html";
		
		public MyOutcomeBuilder(ActionMappingBuilder actionBuilder, OutcomeStatus status){
			this.actionBuilder = actionBuilder;
			this.status = status;
		}
		

		
		@Override
		public ActionMappingBuilder forwardTo(String url) {
			Outcome outcome = new Outcome(status, url,contentType);
			PresenterCommandMappingBuilder.this.mapping.addOutcome(actionBuilder.getActionMame(), status, new FixedOutcomeResolver(outcome));
			return actionBuilder;
		}

		@Override
		public ActionMappingBuilder redirectTo(HttpStatusCode erroCode) {
			
			Outcome outcome = new Outcome(status, erroCode);
			PresenterCommandMappingBuilder.this.mapping.addOutcome(actionBuilder.getActionMame(), status, new FixedOutcomeResolver(outcome));
			return actionBuilder;
		}

		@Override
		public ActionMappingBuilder redirectTo(String url) {
			return this.redirectTo(url, RedirectOptions.temporary());
		}

		@Override
		public ActionMappingBuilder redirectTo(String url, RedirectOptions options) {
			Outcome outcome = new Outcome(
					status,
					url, 
					true,
					options.isPermanent() ? HttpStatusCode.MOVED_PERMANENTLY :  HttpStatusCode.MOVED
			);
			PresenterCommandMappingBuilder.this.mapping.addOutcome(actionBuilder.getActionMame(), status, new FixedOutcomeResolver(outcome));
			return actionBuilder;
		}

		@Override
		public ActionMappingBuilder forwardToLast(String lastAction) {
			PresenterCommandMappingBuilder.this.mapping.addOutcome(actionBuilder.getActionMame(), status, new ForwardToLast(webCommandMappingService, lastAction));
			return actionBuilder;
		}

		@Override
		public ActionMappingBuilder redirectToLast() {
			PresenterCommandMappingBuilder.this.mapping.addOutcome(actionBuilder.getActionMame(),status, new RedirectToLast());
			return actionBuilder;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public OutcomeBuilder withContentAs(String contentType) {
			this.contentType = contentType;
			return this;
		}

	

	}





}
