/**
 * 
 */
package org.middleheaven.util.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.middleheaven.events.EventListenersSet;
import org.middleheaven.util.function.Block;

/**
 * 
 */
public abstract class AbstractProperty<T> implements Property<T>{

	private EventListenersSet<PropertyChangeListener> listeners = EventListenersSet.newSet(PropertyChangeListener.class);

	
	protected final void fireChange(T oldValue, T newValue){
		listeners.broadcastEvent().propertyChange(new PropertyChangeEvent(this, "", oldValue, newValue));
	}

	public final void addListener(PropertyChangeListener listener){
		listeners.addListener(listener);
	}
	
	public final void removeListener(PropertyChangeListener listener){
		listeners.removeListener(listener);
	}
	
	private String name;
	private Class<?> type;
	
	protected AbstractProperty (String name, Class<?> type){
		this.name = name;
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName(){
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getValueType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<T> onChange(final Block<T> trigger) {
		this.addListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				trigger.apply((T) evt.getNewValue());
			}
		});
		
		return this;
	}
	
	/**
	 * @param value2
	 * @param value3
	 * @return
	 */
	protected final boolean valuesDiffer(T a, T b) {
		return a == null ? b != null : !a.equals(b);
	}
}
