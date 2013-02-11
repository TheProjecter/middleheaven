package org.middleheaven.process.web.server;

import java.util.ArrayList;
import java.util.List;

import org.middleheaven.logging.Logger;
import org.middleheaven.process.ContextScope;
import org.middleheaven.process.web.HttpProcessException;
import org.middleheaven.process.web.HttpStatusCode;
import org.middleheaven.process.web.server.action.BasicOutcomeStatus;
import org.middleheaven.process.web.server.action.OutcomeStatus;

public abstract class AbstractInterruptableChain<F> {

	private final List <Chainnable> filters;
	private int current=0;
	private Outcome outcome;
	private boolean interrupted = false;
	private boolean endIsReached = false;
	
	public AbstractInterruptableChain (List <F> filters ,  Outcome defaultOutcome){
		this.filters = new ArrayList<Chainnable>();
		for (F f : filters){
			this.filters.add(new ChainnableAdapter(f));
		}
		this.filters.add(new Chainnable(){

			@Override
			public void doInChain(HttpServerContext context, AbstractInterruptableChain chain) {
				interruptWithOutcome(doFinal(context));
				endIsReached = true;
			}
			
		});
		
		this.outcome = defaultOutcome;
	}
	
	public Outcome getOutcome() {
		return outcome;
	}

	private interface Chainnable {
		void doInChain(HttpServerContext context, AbstractInterruptableChain chain);
	}
	
	private class ChainnableAdapter implements Chainnable{
		
		private F obj;

		public ChainnableAdapter(F obj) {
			this.obj = obj;
		}

		@Override
		public void doInChain(HttpServerContext context, AbstractInterruptableChain chain) {
			 call(obj, context, chain);
		}
	}
	

	private Outcome resolveOutcome (OutcomeStatus status){
		return new Outcome(status,HttpStatusCode.SERVICE_UNAVAILABLE);
	}
	
	public void doChain(HttpServerContext context) throws HttpProcessException{
		if (current<filters.size()){
			current++;
			if(interrupted || endIsReached){
				return;
			}
			
			final Chainnable chainnable = filters.get(current-1);
			try {
				// invoque 
				
				chainnable.doInChain(context, this);
				
			} catch (Exception e){
				Logger.onBookFor(this.getClass()).error(e,"Exception found handling {0}", chainnable.getClass());
				context.getAttributes().setAttribute(ContextScope.REQUEST, "exception", e);
				this.interruptWithOutcome(resolveOutcome(BasicOutcomeStatus.FAILURE));
			} catch (Error e){
				Logger.onBookFor(this.getClass()).fatal(e,"Exception found handling interceptor {0} " , chainnable.getClass());
				context.getAttributes().setAttribute(ContextScope.REQUEST, "exception", e);
				this.interruptWithOutcome(resolveOutcome(BasicOutcomeStatus.ERROR));
			}
			
		} 
	}
	
	public final void interruptWithOutcome(Outcome outcome){
		interrupted  = true;
		this.outcome = outcome;
	}

	protected abstract Outcome doFinal(HttpServerContext context);
	protected abstract void call(F element, HttpServerContext context, AbstractInterruptableChain chain);
	

	
}
