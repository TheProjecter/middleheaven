/**
 * 
 */
package org.middleheaven.quantity.time.clocks;

import java.util.Timer;

import org.middleheaven.runtime.ShutdownHook;

/**
 * 
 */
public class MachineClockShutdownHook implements ShutdownHook {

	private Timer timer;

	public MachineClockShutdownHook (){}
	
	/**
	 * Constructor.
	 * @param timer
	 */
	public MachineClockShutdownHook(Timer timer) {
		this.timer = timer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void OnTermination() {
		timer.cancel();
	}

}
