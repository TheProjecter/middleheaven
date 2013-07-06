package org.middleheaven.events;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.middleheaven.core.reflection.MethodDelegator;
import org.middleheaven.core.reflection.ProxyHandler;
import org.middleheaven.core.reflection.inspection.Introspector;

/**
 * 
 * @param <L> Listener class
 */
public class EventListenersSet<L> implements Iterable<L>{

	private final Set<L> listeners = new CopyOnWriteArraySet<L>();
	private Class<L> listenerType;
	
	public static <T> EventListenersSet<T> newSet(Class<T> listenerType){
		return new EventListenersSet<T>(listenerType);
	}
	
	protected EventListenersSet(Class<L> listenerType){
		this.listenerType = listenerType;
	}
	
	public void addListener (L listener){
		listeners.add(listener);
	}
	
	public void removeListener(L listener) {
		listeners.remove(listener);
	}
	
	public L broadcastEvent(){
		return Introspector.of(listenerType).newProxyInstance(new EventMethodHandler(this));
	}

	private static class EventMethodHandler implements ProxyHandler{

		Set<?> listeners;
		
		public EventMethodHandler(EventListenersSet<?> set) {
			this.listeners = set.listeners;
		}

		@Override
		public Object invoke(Object proxy, Object[] args, MethodDelegator delegator) throws Throwable {
			
			for (Object listener: listeners){
				delegator.invoke(listener, args);
			}
			return null;
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<L> iterator() {
		return this.listeners.iterator();
	}

	/**
	 * 
	 */
	public void removeAll() {
		this.listeners.clear();
	}

}
