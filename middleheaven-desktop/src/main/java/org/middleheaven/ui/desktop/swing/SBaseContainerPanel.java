/**
 * 
 */
package org.middleheaven.ui.desktop.swing;

import java.util.List;

import javax.swing.JComponent;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIContainer;
import org.middleheaven.util.collections.DelegatingList;


/**
 * 
 */
public abstract class SBaseContainerPanel extends SBasePanel implements  UIContainer {


	private static final long serialVersionUID = -6717859099044198714L;

	@Override
	public void addComponent(UIComponent component) {
		component.setUIParent(this);
		this.add((JComponent)component);
	}

	@Override
	public void removeComponent(UIComponent component) {
		this.remove((JComponent)component);
	}
	
	@Override
	public final int getChildrenCount() {
		return this.getComponentCount();
	}
	
	@Override
	public final List<UIComponent> getChildrenComponents() {
		return new DelegatingList<UIComponent>(){

			@Override
			public UIComponent get(int index) {
				return (UIComponent)getComponent(index);
			}

			@Override
			public int size() {
				return getComponentCount();
			}
			
		};
	}
}
