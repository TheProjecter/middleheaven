/**
 * 
 */
package org.middleheaven.ui.models.form;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.CommandListener;
import org.middleheaven.ui.MethodUIActionHandler;
import org.middleheaven.ui.NamingContainer;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UISearch;
import org.middleheaven.ui.events.UIActionEvent;

/**
 * 
 */
public class UIFormComandBuilder {

	/**
	 * 
	 */
	private static final class IdCommandListener implements
			CommandListener {
		/**
		 * 
		 */
		private final String id;

		/**
		 * Constructor.
		 * @param id
		 */
		private IdCommandListener(String id) {
			this.id = id;
		}

		@Override
		public void onCommand(UIActionEvent event) {
		
			UIClient client = UISearch.absolute(event.getSource()).self().first(UIClient.class);
			
			UIComponent nextWindow = ((NamingContainer)client).findContainedComponent(id);
			
			client.getSceneNavigator().show(nextWindow);
		}
	}

	private String name;
	private LocalizableText caption;

	/**
	 * Constructor.
	 * @param name 
	 * @param caption 
	 * @param abstractSheetSetUIFormModel
	 */
	UIFormComandBuilder(String name, LocalizableText caption) {
		//this.abstractSheetSetUIFormModel = abstractSheetSetUIFormModel;
		this.name = name;
		this.caption = caption;
	}

	/**
	 * 
	 */
	public void handleWith(CommandListener handler) {
		//this.abstractSheetSetUIFormModel.addHandler(name, caption , handler);
	}
	
	public void handleWithMethod(Object instance, String methodName) {
		this.handleWith(MethodUIActionHandler.newInstance(instance, methodName));
	}

	/**
	 * @param id the GID of the window to show
	 */
	public void navigateTo(final String id) {
		this.handleWith(new IdCommandListener(id));
	}

}
