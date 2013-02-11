package org.middleheaven.core.dependency;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.middleheaven.logging.Logger;

public class DependencyResolver {

	Logger log;
	public DependencyResolver(Logger log){
		this.log = log;
	}

	/**
	 * Instantiates all dependables with the given starter.
	 * 
	 * @param <T>
	 * @param dependables
	 * @param starter
	 */
	public <T> void resolve (Collection<T> dependables, Starter<T> starter){

		// this is a very simplistic algorithm that does not 
		// resolve dependencies explicitly. 
		// A stack is filled with the dependencies in order given by the starter;
		// dependencies are then resolved in that order. 
		// if a dependency fails, its moved to another stack.
		//  at the end of iterating the main stack if the new stack has a different number of members 
		// the process is repeated. 
		// If the two stacks have the same items this means those items have a ciclic dependency.
		// proxies will them be used in order to overcome the cycle.

		final List<T> stack = starter.sort(dependables);
		LinkedList<T> newStack  = new  LinkedList<T>(stack);
		LinkedList<T> oldStack  = new  LinkedList<T>();

		List<T> failedDependencies = new LinkedList<T>();

		int oldCount = 0;
		while ( oldCount != newStack.size()) {
			oldStack = newStack; 
			oldCount = newStack.size();
			newStack = new  LinkedList<T>();
			for (Iterator<T> it = oldStack.iterator();  it.hasNext(); ){
				T dependable = it.next();
				try{
					starter.inicialize(dependable);
					it.remove();
				} catch (InicializationNotResolvedException e){
					// dependencies not available yet 
					it.remove();
					newStack.addLast(dependable);
				}  catch (InicializationNotPossibleException e){
					log.warn(e,"Impossible to inicialize {0}." , dependable);
					// dependencies will never be available
					failedDependencies.add(dependable);
				}  
			}
		}

		processFailedDependencies(failedDependencies, starter);

		// proxy phase
		if (!newStack.isEmpty()){
			for (Iterator<T> it = newStack.iterator();  it.hasNext(); ){
				T dependable = it.next();
				try{
					starter.inicializeWithProxy(dependable);
					it.remove();
				} catch (InicializationNotResolvedException e){
					// dependencies not available yet 
					it.remove();
					newStack.addLast(dependable);
				}  catch (InicializationNotPossibleException e){
					log.warn(e,"Impossible to inicialize {0}", dependable);
					// dependencies will never be available
					failedDependencies.add(dependable);

				}  
			}
		}

		processFailedDependencies(failedDependencies, starter);


	}

	private <T> void processFailedDependencies (List<T> failedDependencies, Starter<T> starter){

		for (Iterator<T> it  = failedDependencies.iterator(); it.hasNext(); ){
			
			final T next = it.next();
			if (!starter.isRequired(next)){
				it.remove();
			} else {
				log.fatal("Impossible to resolve dependencies for " + next );
			}
		}

		if (!failedDependencies.isEmpty()){
			throw new DependencyResolutionFailedException(failedDependencies);
		}

	}
	

}
