/**
 * 
 */
package org.middleheaven.runtime;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 */
public class ShutdownHooks {

	
	private static List<ShutdownHook> hooks = new CopyOnWriteArrayList<ShutdownHook>();
	private static ShutdownHook masterHook = new ShutdownHook() {
		
		@Override
		public void OnTermination() {
			for(ShutdownHook hook : hooks){
				hook.OnTermination();
			}
		}
	};
	private static RuntimeAdapter adapter = new StandaloneRuntimeAdapter(masterHook);
	
	private ShutdownHooks(){}
	
	// TODO protect with priveleg
	public static void setRuntimeAdapter (RuntimeAdapter otherAdapter){
		adapter = otherAdapter;
		adapter.add(masterHook);
	}
	
	public static void addShutdownHook (ShutdownHook hook){
		hooks.add(hook);
	}
	
	public static void removeShutdownHook (ShutdownHook hook){
		hooks.remove(hook);
	}
}


