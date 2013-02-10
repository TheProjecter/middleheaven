package org.middleheaven.ui.layout;

import org.middleheaven.ui.ComponentAggregationEvent;
import org.middleheaven.ui.UISize;

/**
 * Flow Layout
 */
public class UIFlowLayoutModel  {

	
	public UIFlowLayoutModel (){}
	

	public void componentAdded(ComponentAggregationEvent event) {}


	public void componentRemoved(ComponentAggregationEvent event) {}


	public UISize getLayoutablePreferedSize(UISize layoutable) {
		return layoutable;
	}


}
