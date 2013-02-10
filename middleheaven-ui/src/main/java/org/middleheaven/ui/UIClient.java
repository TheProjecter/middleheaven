package org.middleheaven.ui;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.components.UIContainer;




/**
 * The client graphical interface the user interacts with in order
 * to input commands and receive informations.
 * 
 */
public interface UIClient extends UIComponent, UIContainer, NamingContainer{

	/**
	 * Causes the UIClient to terminate.
	 */
	public void terminate();
	
	
	public SceneNavigator getSceneNavigator();
	

	/**
	 * If the client has and shows a splash window.
	 * @return <code>true</code> if the client has and shows a splash window, <code>false</code> otherwise.
	 */
	public boolean isSplashWindowUsed();
	
	/**
	 * 
	 * @return Unrendered UIWindow
	 */
	public abstract UIComponent resolveMainWindow(UIClient client,AttributeContext context);

	/**
	 * Splash window. If unrendered will be rendered 
	 * @param client 
	 * @return splash window.
	 */
	public abstract UIComponent resolveSplashWindow(UIClient client, AttributeContext context);
}
