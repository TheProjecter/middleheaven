package org.middleheaven.ui.web;

import org.middleheaven.ui.AbstractUIClient;
import org.middleheaven.ui.SceneNavigator;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UIBrowser;

public class Browser extends AbstractUIClient implements UIBrowser{

	
	
	private SceneNavigator navigator;

	public Browser(SceneNavigator navigator) {
		this.navigator = navigator;
	}

	@Override
	public UISize getDisplayableSize() {
		return UISize.pixels(0, 0);
	}

	@Override
	public void terminate() {
		// no-op
	}
	
    public boolean isRendered(){
    	return true;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SceneNavigator getSceneNavigator() {
		return navigator;
	}

	
}
