/**
 * 
 */
package org.middleheaven.ui.binding;

import org.middleheaven.core.reflection.MethodHandler;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.events.UIActionEvent;

/**
 * 
 */
public final class MethodCommandListener implements CommandListener {

	
	private MethodHandler method;
	private Object target;
	
	public MethodCommandListener (MethodHandler method, Object target){
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
