package org.middleheaven.aas;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class CallbacksSet implements Iterable<Callback> , Serializable{


	private static final long serialVersionUID = -1702719080774468738L;
	
	private final Set<Callback> callbacks = new HashSet<Callback>();
	
	public CallbacksSet(){}
	
	public void add(Callback callback){
		callbacks.add(callback);
	}
	
	public void remove(Callback callback){
		callbacks.remove(callback);
	}

	@Override
	public Iterator<Callback> iterator() {
		return callbacks.iterator();
	}
	
	/**
	 * 
	 * @return <code>true</code> if none of the {@link Callback}s are filled.
	 */
	public boolean isBlank(){
		boolean isBlank = false;
		for(Callback c : this){
			isBlank = isBlank | c.isBlank();
		}
		return isBlank;
	}

	public <T extends Callback> T getCallback(Class<T> type) {
		for(Callback c : this){
			if(type.isInstance(c)){
				return type.cast(c);
			}
		}
		return null;
	}

	/**
	 * 
	 * @return <code>true</code> if no {@link Callback}s are defined.
	 */
	public boolean isEmpty() {
		return this.callbacks.isEmpty();
	}
}
