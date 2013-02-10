package org.middleheaven.ui.components;

import org.middleheaven.ui.UIPrespective;

public interface UIWindow extends UIPrespective{

	public void addUIWindowListener(UIWindowsListener listener);
	public void removeUIWindowListener(UIWindowsListener listener);

	public Iterable<UIWindowsListener> getUIWindowListeners();
	
}
