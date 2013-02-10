/**
 * 
 */
package org.middleheaven.ui.web.html;

import org.middleheaven.ui.components.UICommand;
import org.middleheaven.ui.events.UIActionEvent;

/**
 * 
 */
public interface HtmlUICommand extends HtmlUIComponent {

	public void fireEvent(UIActionEvent event);
	
}
