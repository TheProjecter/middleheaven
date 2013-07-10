/**
 * 
 */
package org.middleheaven.ui;

import java.lang.reflect.Method;

import org.middleheaven.ui.events.UIActionEvent;

/**
 * 
 */
public class MethodUIActionHandler implements CommandListener {

	public static  MethodUIActionHandler newInstance(Object instance,Method method){
		return new MethodUIActionHandler(instance, method);
	}

	public static  MethodUIActionHandler newInstance(Object instance,String name){
		
		Method method = null;
		for (Method m : instance.getClass().getMethods()){
			if (m.getName().equals(name)){
				method = m;
				break;
			}
		}
		
		// TODO not exists
		return new MethodUIActionHandler(instance, method);
	}
	
	private Method method;
	private Object instance;
	
	/**
	 * Constructor.
	 * @param method
	 */
	public MethodUIActionHandler(Object instance, Method method) {
		this.method = method;
		this.instance = instance;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCommand(UIActionEvent event) {
		
		Class<?>[] types = method.getParameterTypes();
		Object[] args =  new Object[types.length];
		
		for (int i=0;i < types.length; i++){
			
			if (types[i].isAssignableFrom(UIActionEvent.class)){
				args[i] = event;
			} else {
				args[i] = null; // TODO
			}
		}
		
		try {
			method.invoke(instance, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
