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
	private L listener;
	
	public static <T> EventListenersSet<T> newSet(Class<T> listenerType){
		EventListenersSet<T> set = new EventListenersSet<T>();
		set.listener  = Introspector.of(listenerType).newProxyInstance(new EventMethodHandler(set));
		return set;
	}
	
	protected EventListenersSet(){}
	
	public void addListener ( L listener){
		listeners.add(listener);
	}
	
	public void removeListener(L listener) {
		listeners.remove(listener);
	}
	
	public L broadcastEvent(){
		return listener;
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

}
