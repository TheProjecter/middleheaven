/**
 * 
 */
package org.middleheaven.ui.binding;

import org.middleheaven.reflection.ReflectedMethod;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.events.UIActionEvent;

/**
 * 
 */
public final class MethodCommandListener implements CommandListener {

	
	private ReflectedMethod method;
	private Object target;
	
	public MethodCommandListener (ReflectedMethod method, Object target){
		this.method = method;
		this.target = target;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCommand(UIActionEvent event) {
		method.invoke(target, event);
	}

}
