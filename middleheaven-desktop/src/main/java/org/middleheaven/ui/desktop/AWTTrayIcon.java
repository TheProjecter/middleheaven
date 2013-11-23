package org.middleheaven.ui.desktop;

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.util.Collections;
import java.util.List;

import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UIPosition;
import org.middleheaven.ui.UISize;
import org.middleheaven.ui.components.UIDesktopTrayIcon;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;

class AWTTrayIcon extends TrayIcon implements UIDesktopTrayIcon{

	private String id;
	private UIComponent parent;
	private String family;
	private final Property<Boolean> visible = ValueProperty.readOnly("visible", true);
	private final Property<Boolean> enable = ValueProperty.readOnly("enable", true);

	public AWTTrayIcon(Image image, String tooltip, PopupMenu popup) {
		super(image, tooltip, popup);
	}
	
	@Override
	public List<UIComponent> getChildrenComponents() {
		return Collections.emptyList();
	}

	@Override
	public int getChildrenCount() {
		return 1;
	}

	@Override
	public String getFamily() {
		return family;
	}

	@Override
	public String getGID() {
		return id;
	}

	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIDesktopTrayIcon.class;
	}

	@Override
	public UIComponent getUIParent() {
		return parent;
	}

	@Override
	public boolean isRendered() {
		return true;
	}


	@Override
	public void setFamily(String family) {
		this.family = family;
	}

	@Override
	public void setGID(String id) {
		this.id = id;
	}

	@Override
	public void setUIParent(UIComponent parent) {
		this.parent = parent;
	}

	@Override
	public UISize getDisplayableSize() {
		return UISize.pixels(this.getSize().width, this.getSize().height);
	}
	
	@Override
	public UIPosition getPosition() {
		return UIPosition.pixels(0,0);
	}

	@Override
	public void setDisplayableSize(UISize size) {
		throw new UnsupportedOperationException("Tray icon cannot be resized");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isType(Class<? extends UIComponent> type) {
		return type.isAssignableFrom(this.getComponentType());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getVisibleProperty() {
		return visible;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<Boolean> getEnableProperty() {
		return enable;
	}

}
