package org.middleheaven.chart;

import org.middleheaven.events.EventListenersSet;

public abstract class AbstractDataset implements Dataset {

	EventListenersSet<DatasetListener> listeners = EventListenersSet.newSet(DatasetListener.class);
	
	public AbstractDataset(){
	}
	
	protected synchronized void fireChangeEvent(){
		DatasetChangeEvent event = new DatasetChangeEvent(this);
		listeners.broadcastEvent().onChange(event);
	}
	
	@Override
	public void addDatasetListener(DatasetListener listener) {
		listeners.addListener(listener);
	}
	
	@Override
	public void removeDatasetListener(DatasetListener listener) {
		listeners.removeListener(listener);
	}

	@Override
	public void onChange(SeriesChangeEvent event) {
		this.fireChangeEvent();
	}

}
