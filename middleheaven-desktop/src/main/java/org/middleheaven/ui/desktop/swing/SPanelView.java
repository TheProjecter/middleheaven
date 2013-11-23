package org.middleheaven.ui.desktop.swing;

import org.middleheaven.global.text.LocalizableText;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.UILayoutConstraint;
import org.middleheaven.ui.UIPrespectiveListener;
import org.middleheaven.ui.components.UILayout;
import org.middleheaven.ui.components.UIView;
import org.middleheaven.ui.property.Property;
import org.middleheaven.ui.property.ValueProperty;

public class SPanelView extends SBaseContainerPanel implements UIView {


	private static final long serialVersionUID = 1L;
	private Property<LocalizableText> text = ValueProperty.writable("text", LocalizableText.class);


	@Override
	public <T extends UIComponent> Class<T> getComponentType() {
		return (Class<T>) UIView.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUIContainerLayout(UILayout component) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UILayout getUIContainerLayout() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponent(UIComponent component,
			UILayoutConstraint layoutConstrain) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPrespectiveListener(UIPrespectiveListener listener) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePrespectiveListener(UIPrespectiveListener listener) {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<UIPrespectiveListener> getPrecpectiveListeners() {
		throw new UnsupportedOperationException("Not implememented yet");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property<LocalizableText> getTitleProperty() {
		return text;
	}
	

}
