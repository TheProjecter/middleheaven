package org.middleheaven.test.ui.web;

import org.middleheaven.web.processing.HttpCode;
import org.middleheaven.web.processing.HttpContext;
import org.middleheaven.web.processing.action.BasicOutcomeStatus;
import org.middleheaven.web.processing.action.Interceptor;
import org.middleheaven.web.processing.action.InterceptorChain;
import org.middleheaven.web.processing.action.PresenterCommandMappingBuilder;



public class TesteWriting {

	
	public void testWriting(){
		
		PresenterCommandMappingBuilder.map(ProjectPresenter.class)
		.to("/projeto.*")
		.with(new ProjectInterceptor())
		.withAction("save")
		.on(BasicOutcomeStatus.SUCCESS).forwardTo("project.list.html")
		.on(BasicOutcomeStatus.FAILURE).forwardTo("genericfailure.html");
		
		PresenterCommandMappingBuilder.map(ProjectPresenter.class)
		.to("/projeto.*")
		.with(new ProjectInterceptor())
		.withAction("save")
		.on(BasicOutcomeStatus.SUCCESS).forwardTo("project.list.html")
		.on(BasicOutcomeStatus.FAILURE).redirectTo(HttpCode.NOT_FOUND);

	}
	
	
	private static class ProjectPresenter{
		
	}
	
	private static class ProjectInterceptor implements Interceptor{

		@Override
		public void intercept(HttpContext context, InterceptorChain chain) {
			// TODO implement ProjectInterceptor.intercept
			
		}
		
	}
}
