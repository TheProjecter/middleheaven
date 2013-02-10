/**
 * 
 */
package org.middleheaven.ui;

import org.middleheaven.ui.events.UIPrespectiveEvent;

/**
 * 
 */
public interface UIPrespectiveListener {

	public void onOpen(UIPrespectiveEvent event);
	public void onClosed(UIPrespectiveEvent event);
	public void onClosing(UIPrespectiveEvent event);
	public void onDeiconified(UIPrespectiveEvent event);
	public void onIconified(UIPrespectiveEvent event);
	
}
