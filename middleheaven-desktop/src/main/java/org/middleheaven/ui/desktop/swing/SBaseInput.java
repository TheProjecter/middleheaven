package org.middleheaven.ui.desktop.swing;

import java.util.Collections;
import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.components.UIInput;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;

public abstract class SBaseInput extends SBaseOutputPanel implements UIInput  {

	private static final long serialVersionUID = -6023436544771099799L;
	

	private final Property<String> name = ValueProperty.writable("name", String.class);
	
	public SBaseInput(){
	}

	public Property<String> getNameProperty(){
		return name;
	}

	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.emptyList();
	}

	@Override
	public int getChildrenCount() {
		return 0;
	}

	@Override
	public boolean isRendered() {
		return true;
	}
}
