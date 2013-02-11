package org.middleheaven.chart;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractSeries implements Series {

	List<SeriesListener> listeners = new CopyOnWriteArrayList<SeriesListener>();
	String name;
	
	public AbstractSeries(String name){
		this.name = name;
	}
	
	protected synchronized void fireChangeEvent(){
		SeriesChangeEvent event = new SeriesChangeEvent(this);
		for (SeriesListener l : listeners){
			l.onChange(event);
		}
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public void addSeriesListener(SeriesListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeSeriesListener(SeriesListener listener) {
		listeners.remove(listener);
	}

	public void setName(String name){
		this.name=name;
		fireChangeEvent();
		
	}



}
