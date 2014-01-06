package org.middleheaven.quantity.time.clocks;

import org.middleheaven.quantity.time.TimePoint;

public interface ClockTickListener {

	public void onTick (TimePoint point);
}
