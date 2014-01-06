/**
 * 
 */
package org.middleheaven.runtime;

/**
 * 
 */
class ShutdownHookThread extends Thread {

	private StandaloneRuntimeAdapter runtimeAdapter;

	/**
	 * Constructor.
	 * @param defaultRuntimeAdapter
	 */
    ShutdownHookThread(StandaloneRuntimeAdapter defaultRuntimeAdapter) {
		this.runtimeAdapter = defaultRuntimeAdapter;
	}

	@Override
	public void run(){
		runtimeAdapter.terminate();
	}
}
