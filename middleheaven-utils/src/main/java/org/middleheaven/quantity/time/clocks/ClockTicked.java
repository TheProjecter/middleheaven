package org.middleheaven.quantity.time.clocks;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.quantity.time.TimePoint;


public class ClockTicked {

	
	protected ClockTicked(){}
	
	Set<ClockTickListener> listeners = new CopyOnWriteArraySet<ClockTickListener>();
	
	public void addClockTickListener (ClockTickListener listener){
		listeners.add(listener);
	}
	
	public void removeClockTickListener (ClockTickListener listener){
		listeners.remove(listener);
	}

	public void tick(TimePoint point ) {
		// TODO use agregate exception
		RuntimeException firstException = null;
		for (ClockTickListener c : listeners){
			try{
				c.onTick(point);
			} catch (RuntimeException e){
				if(firstException==null){
					firstException = e;
				}
			}
		}
		
		if(firstException!=null){
			throw firstException;
		}
	}
}
