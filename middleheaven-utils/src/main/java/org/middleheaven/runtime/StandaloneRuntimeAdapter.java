/**
 * 
 */
package org.middleheaven.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 */
public class StandaloneRuntimeAdapter implements RuntimeAdapter {

	private static List<ShutdownHook> hooks = new CopyOnWriteArrayList<ShutdownHook>();
	
	/**
	 * Constructor.
	 * @param masterHook
	 */
	public StandaloneRuntimeAdapter(ShutdownHook masterHook) {
		hooks.add(masterHook);
	
		Runtime.getRuntime().addShutdownHook (new ShutdownHookThread (this));
	}
	
	void terminate(){
		List<RuntimeException> exceptions = new ArrayList<RuntimeException>(0);
		for(ShutdownHook hook : hooks){
			try{
				hook.OnTermination();
			} catch (RuntimeException e){
				exceptions.add(e);
			}
		}
		throw new AggregateException(exceptions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(ShutdownHook hook) {
		hooks.add(hook);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(ShutdownHook hook) {
		hooks.remove(hook);
	}

}
